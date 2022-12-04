#!/bin/bash

SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3307/aniquiz
DSPRING_DATASOURCE_USERNAME=aniquiz
DSPRING_DATASOURCE_PASSWORD=test
RSA_PRIVATE_KEY=$(cat <<-EOF
-----BEGIN PRIVATE KEY-----
MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC8Uktdery5VFRq
J/RJ/yhnZa1FfGMIZp0NUxXN6MZhw7LvZ8zZul4BTVJ2chyIhZNTLOD+E55mTF7O
ec0a4lxc5a7n1E0YZXbMP6fkgJMkg4B3urJluozYuQzkqfroOdfK3GMekuv65zuu
7rqAR/aqm+uhaYLh3xDPdMCfnZLkIEvnIj4NzOJPH/cBunrbU8vDJHTR8pIsZABM
2ay11f3+RxbPSDdnUE7sgppsLSJNE5jjGd2zo/SXr5KtsteDJkzKdc5+iXapRFPL
NCu4DM8tdX21ly2phhFXggzdM2o7D8A7TUirw5nRnciWLGx7rTkWZNN4ro2GvMnw
c5BkM30RAgMBAAECggEACgQAcRzNe7fR84FjeyoAXptoPzlR1QkMEkDYeIaIQnvT
gOsO2IeSZ1RS7ey8usAL6VEKOEkX1A38vtN4hpUCX4hxqG/Al+sRieRazArQiJEb
3IJH6aQlyILy/hhSOVaWNPpKpT4gIcx5fqDAt4DcTA+V1RRU+ylPbhEiC3sobEx6
Ech06uQa3PyGSTm3jcaYwboW/Kf2v0UZE00Xw0+61FBp11tKtBXnFUK9abCssg1b
s9CENTcU5cRsfyDWKtABzJZACTck0YY3Lydc4Z21N593Ws7ilWa4IzX/8iuNhH/1
/KNNB6LQ6g/HdkGD8CpK5I7O4h5SwBR4B+8lXO5gAQKBgQDdenNSz1DRIP7X8ogF
JfHLTmhZLWAyXwD8zpTWovjkjRAx7PzjHuUKXxP1C3qA6bUN5NcqY63ZIoUOY03+
0HKsMoZlKv0Pvjv76jQe+LEPi8l4sGnHVzL0SSsT+CIBjRAIHjP/801HUN+JVRkO
y94SAIQiCYFhvOsdGhx8A0p5EQKBgQDZrMz7FUd11TFxfEQRDLy05E/w/R2sgCsh
uWGo1lgAuVvAzTSE/MK7HvGpt1vl0wFIeWDsG1CyLquB3O7nx84PYbDctt9bqxgn
5At33yeKNg70XUJzQ5OGFy4WXxBUg7tN3Xjvx9xWLyp4PyGG6f/FOmckdTh91f7D
d1LC2LjEAQKBgGTv5Zcb/M0SqJJqaitOe8AYX94km+7Y9W5CB/s6Ewy2bWcThr1L
2z66zjGjpZUS7f0DCjy7aQPIg7Nm9qjm/xwTesSoruR54oSQ3GgxFyDqqEtkuxCq
YQKMRiG3uGfUjjXk7fX8QH/8D6RbMCxaJT83FQEeK+ULYGvG9qxeI5bBAoGAec5R
kfsRVuWq2kdgS24mNs8/0KnDLiE2gZOcC3+OFTGkH1p1FineyhkBwCfDu/J8WFLb
oqOSX3LwNCYuZfMP4tmWv5txl3lBdikNJdHw7U1cxu9c3OUmqzVTa99cYv6oFGVn
ZD4hlPSlvk4Nc9KDdpk82FDkAL575mNiI3hkxAECgYEAqYYfszbBQsyludtACdoY
3eIBuUEJJkIF2qdciJaDZ23P53REv97lW0nEIV8RzALEK4hoDy0v025t0xQe8mlC
f4z2VgwBmJktTtNzGgkPsQIh8aHrdM3WIF5DEhZPR9k/RmQZt/f5CnNZ3+z/xvwN
1b4LCmP5iSsrO8UIT142Q9w=
-----END PRIVATE KEY-----
EOF
)
RSA_PUBLIC_KEY=$(cat <<-EOF
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvFJLXXq8uVRUaif0Sf8o
Z2WtRXxjCGadDVMVzejGYcOy72fM2bpeAU1SdnIciIWTUyzg/hOeZkxeznnNGuJc
XOWu59RNGGV2zD+n5ICTJIOAd7qyZbqM2LkM5Kn66DnXytxjHpLr+uc7ru66gEf2
qpvroWmC4d8Qz3TAn52S5CBL5yI+DcziTx/3Abp621PLwyR00fKSLGQATNmstdX9
/kcWz0g3Z1BO7IKabC0iTROY4xnds6P0l6+SrbLXgyZMynXOfol2qURTyzQruAzP
LXV9tZctqYYRV4IM3TNqOw/AO01Iq8OZ0Z3Ilixse605FmTTeK6NhrzJ8HOQZDN9
EQIDAQAB
-----END PUBLIC KEY-----
EOF
)

mvn \
-DSPRING_DATASOURCE_URL=$SPRING_DATASOURCE_URL \
-DSPRING_DATASOURCE_USERNAME=$DSPRING_DATASOURCE_USERNAME \
-DSPRING_DATASOURCE_PASSWORD=$DSPRING_DATASOURCE_PASSWORD \
-DRSA_PRIVATE_KEY="$RSA_PRIVATE_KEY" \
-DRSA_PUBLIC_KEY="$RSA_PUBLIC_KEY" \
clean test