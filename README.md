Aniquiz is a multilingual anime/manga quiz API written in Java. I founded it as none exists at the moment. Why I need it ? Because this API will be part of a fun and innovative mobile game in the future. 

## Quick start

### Clone the repository

```sh
git clone git@github.com:lamai6/aniquiz.git <YOUR_PROJECT_NAME>
```
```sh
cd <YOUR_PROJECT_NAME>
```

### Start dev environment

[Docker](https://docs.docker.com/get-docker/) is required to run all needed containers: one container for the API, and 2 others for dev and test databases. 

You will find an `.env` file containing all sensitive information that will be injected in containers' environment. You can modify it as desired.

Build dev environment by running the following compose file:

```sh
docker compose -f dev.docker-compose.yml up -d
```

The API is available at `localhost:8080`.

### Use existing or create new contributor account

When containers are up and running, a contributor account with admin rights is already persisted in the database with `admin` as username and password, and `admin@aniquiz.fr` as email (only for demo purposes, obviously).

If you prefer to register your own contributor account (which will not have admin rights), hit `contributors` endpoint with your credentials:

```sh
curl --location --request POST 'localhost:8080/contributors' \
--header 'Content-Type: application/json' \
--data-raw '{"username": "<YOUR_USERNAME>", "email": "<YOUR_EMAIl>", "password": "<YOUR_PASSWORD>"}'
```

### Get authentication token

The API is secured by JWT authentication. Generate your JWT, which will be valid for 7 days, and include it in the header of all your future requests.

```sh
curl --location --request POST 'localhost:8080/token' \
--header 'Content-Type: application/json' \
--data-raw '{"email":"<YOUR_EMAIL>","password":"<YOUR_PASSWORD>"}'
```

### Add your first series

The API will respond to the request below with the new series id.

```sh
curl --location --request POST 'localhost:8080/series' \
--header 'Authorization: Bearer <YOUR_TOKEN>' \
--header 'Content-Type: application/json' \
--data-raw '{"name": "One Piece", "author": "Eichiro Oda", "release_date": "1999-10-20"}'
```

### Add your first question for this series

It is the same here as above.

```sh
curl --location --request POST 'localhost:8080/questions' \
--header 'Authorization: Bearer <YOUR_TOKEN>' \
--header 'Content-Type: application/json' \
--data-raw '{"difficulty":"E","series":{"name":"One Piece"},"titles":[{"language":{"code":"en"},"propositions":[{"name":"10,000,000","correct":false},{"name":"30,000,000","correct":true},{"name":"50,000,000","correct":false},{"name":"120,000,000","correct":false}],"name":"How much is Luffy'\''s first bounty?"}],"type":"SCQ"}'
```

### Retrieve the question you've just added

```sh
curl --location --request GET 'localhost:8080/questions/<NEW_QUESTION_ID>' \
--header 'Authorization: Bearer <YOUR_TOKEN>'
```

## FAQ

### How to run tests ?

The project is packaged by feature, so you will find an integration test and unit tests per feature.

To run all tests:

```sh
./run_tests.sh
```

### How to generate RSA public and private keys ?

```sh
openssl genrsa -out keypair.pem 2048
openssl rsa -in keypair.pem -pubout -out public.pem
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem
```

### How to fetch the logs of a container ?

```sh
docker logs -f -n <NUMBER_OF_LINES> <CONTAINER_NAME>
```

### How to stop the dev environment ?

```sh
docker compose -f dev.docker-compose.yml down
```
