Feature: Question

  Scenario: Adding a new easy single-choice question
    Given I am a contributor
    And I want to add a question
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

  Scenario: Rejecting contributor multiple-choice question because only one proposition is correct
    Given I am a contributor
    And I want to add a question
      | title                                                  | series             | type | difficulty | language |
      | What is the distance between Wall Maria and Wall Sina? | Shingeki no Kyojin | MCQ  | H          | en       |
    And the propositions of this question are
      | name | correct |
      | 110  | false   |
      | 190  | false   |
      | 230  | true    |
      | 350  | false   |
    When I submit the question
    Then the question is rejected
