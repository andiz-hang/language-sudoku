TDD examples
================

TDD Example 1 - Student learning Chinese
User Story: As a student learning Chinese, I want a way to be able to see the words of numbers in both English and Chinese, but not at the same time to help me better memorize the words.
Example: Users are able to toggle between English and Chinese, which changes the language the words in the popup menu are in.
Example: The toggle function only allows the user to see the numbers in one language at a time, which promotes memorization of the characters.
Example: The answer chosen under a toggle function should not affect the language type for the blank cells in the current puzzle.

TDD Example 2 - Memorization Enhancement
User Story: As a language learner, I want multiple methods to train myself to memorize.
Example: In initial mode, the prefilled cells should be filled with the user's native language, (English). And to solve the puzzle, the language being studied (Chinese) should be filled in the blank cells.
Example: In the enhanced mode, the prefilled cells should be filled with the language being studied, and solving the puzzle by using the user's native language. Then, the user can back check their memorization.

TDD Example 3 - User with small screen
User Story: As a user with a cellphone that has a small screen, I want the app to have visual cues to help me play the game.
Example: The app has included the ability to zoom in onto a specific button. When the user clicks on a button, it will zoom in onto that button to allow for the user to pick a number.

TDD Example 4 - Filling answer
User Story: As a game player, when I pick an answer to fill a cell, I want a hint to remind me that if I choose an obviously wrong answer or the answer chosen is valid without any conflict.  Example: Whenever the user inputs an incorrect answer, the cell will be highlighted in red to warn the user the answer chosen is wrong.
Example: When the user chooses a wrong answer, the contradicted row or columns or blocks will highlighted in dark red which distinguishes from the targeted cell; When the user chooses an valid answer, no highlighting at all.
Example: When the user erase a wrong answer in the cell, all the highlighted cells should be resumed.

TDD Example 5 - Different views based on devices
User Story: As a vocabulary learner practicing at home, I want to use my tablet for Sudoku vocabulary practice, so that the words will be conveniently displayed in larger, easier to read fonts.
Example: The layout of the puzzle as well as the size of each sudoku cells should based on the devices screen size rather than a fix-numbered measurement.

TDD Example 6 - Screen orientation
User Story: As a vocabulary learner taking the bus, I want to use my phone in landscape mode for Sudoku vocabulary practice, so that longer words are displayed in a larger font that standard mode.
Example: In landscape mode, the cells will reset to fit the whole screen so that the words could have more space and show in a larger font.

TDD Example 7 - User defined vocabulary list
User Story: As a teacher, I want to specify a list of word pairs for my students to practice this week.
User Story: As a student working with a textbook, I want to load pairs of words to practice from each chapter of the book.
User Story: As a student, I want the Sudoku app to keep track of the vocabulary words that I am having difficulty recognizing so that they will be used more often in my practice puzzles.
Example: There will be a button leading the user to choose its local csv file, and to upload.
Example: When uploading, the number of word pairs should be check to make sure there are at least 9 pairs being uploaded. If more than 9 pairs, only 9 pairs will be chosen randomly. The system will parse and store the given word list and update the word list submenu.
Example: A toast should show up to tell if the user uploaded successfully or not.
Example: There could be a button leading the user to see the current vocabulary list either the build-in one or the user uploaded one. The word pairs will list up in the order based on the recognizing difficulty of the user which recorded as the times whenever clicking the wrong answers. (not implemented yet)

TDD Example 8 - Listening Comprehension
User Story: As a student who wants to practice my understanding of spoken words in the language that I am learning, I want a listening comprehension mode. In this mode, numbers will appear in the prefilled cells and the corresponding word in the language that I am learning will be read out to me when I press the number.
Example: In the puzzle, there will be a button for listening comprehension mode. This mode will show numbers in prefilled cells. Click the number, the relevant word will be read out.
