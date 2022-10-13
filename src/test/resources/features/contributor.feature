Feature: Contributor

  Scenario: Registering a new contributor
    Given the contributor username is "roronoa95"
    And its email is "roronoa95@gmail.com"
    And its password is "onepiece"
    When the contributor registers
    Then the contributor is registered

  Scenario: Rejecting contributor registration because of invalid email
    Given the contributor username is "roronoa95"
    And its email is "roronoa95gmailcom"
    And its password is "onepiece"
    When the contributor registers
    Then the registration is rejected
