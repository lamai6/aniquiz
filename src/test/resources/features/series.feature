Feature: Series

  Scenario: Adding a new series
    Given the series name is "One Piece"
    And its release date is 1999-10-20
    When the user send the series
    Then the series should be added
