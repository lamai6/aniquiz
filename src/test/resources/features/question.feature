Feature: Question

  Scenario: Adding a new question
    Given I want to add a new easy single-choice question, as a contributor
      | title                             | series    | type | difficulty | language |
      | How much is Luffy's first bounty? | One Piece | SCQ  | E          | en       |
    And the propositions of this question are
      | name        | correct |
      | 10,000,000  | false   |
      | 30,000,000  | true    |
      | 50,000,000  | false   |
      | 120,000,000 | false   |
    When I submit the question
    Then the question is added to the API
