# Successorator Application

## Overview
Successorator is a task management application designed to help users focus on their Most Important Tasks (MITs) for the day. With features such as entering multiple MITs, tracking completed tasks, and carrying over unfinished tasks to the next day, Successorator aims to enhance productivity and ensure that users can keep track of their progress over time.

## Features
- **Single MIT Success List**: Allows users to enter and check off their MIT for the day.
- **Multiple MITs**: Supports entering and checking off multiple MITs to ensure all critical tasks are managed.
- **Show Completed MITs**: Users can view their finished MITs for the day, providing a sense of accomplishment.
- **Date Display**: Displays the current day of the week and date for better orientation.
- **Carry Over Unfinished MITs**: Unfinished MITs automatically roll over to the next day.
- **Persist MITs**: Tasks are saved and persist even after the app restarts.
- **Move Finished MIT Back to Unfinished**: Offers flexibility in task management by allowing users to reclassify completed tasks as unfinished.
- **UI Mock for Date**: Includes a feature for manually advancing the date to test date-related functionalities.

## Software Design
- **Code Quality**: Ensures tidy, well-indented code with consistent naming and necessary comments.
- **MVP Design Pattern**: Utilizes the Model-View-Presenter (MVP) pattern for a clean architectural approach.
- **SRP and DRY**: Emphasizes the Single Responsibility Principle and Don't Repeat Yourself to maintain a clean and efficient codebase.
- **Requirement Reflection**: The codebase is closely aligned with the outlined requirements, ensuring full coverage of functionalities.

## Testing
- **Comprehensive Coverage**: Implements tests covering all non-trivial methods, excluding simple getters/setters.
- **Local Testing**: Integrates testing with tools like JUnit, Espresso, or Robolectric to facilitate automated local testing.
- **Continuous Integration**: Utilizes GitHub Actions for running tests, ensuring consistent code quality and functionality

## Velocity

Estimation of initial Velocity with justification
- 7 hours/week per person
- 80 total hours for the project
- 0.467 velocity (Velocity based off of I2 of Milestone)

Some of the main concerns we had:
Figuring out how to implement the logic in some of the features may require some dedicated thinking time
We expect communication between the teammates to be relatively frequent, which will take up time
Running, testing, and debugging the code (especially understanding bugs) will take extra time
Restructuring and refactoring code to allow us to add new functionalities

Iteration length is one week
40 work-hours distributed amongst 6 members (~6 hours per person/week)
Work hours per iteration = 0.467 velocity * 36 work hours = 18.6 work hours per iteration
Velocity based off of I2 of Milestone

Reflection:
We stayed relatively up-to-date with our proposed and calculated velocity from our I2 from M1. The velocity accounted for our initial concerns and was estimated from our real velocity from the previous milestone. For this milestone, the I1 velocity was closer to 0.44 as we worked roughly 40 hours and estimated 17.5 total work hours for I1. Our I2 velocity was closer to 0.5 as we worked roughly 35 hours cumulatively and estimated 17.5 total work hours. 

