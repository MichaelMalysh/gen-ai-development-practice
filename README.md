## Prompt Exploring results

### 1. Give me 7 awesome books about history

Using such prompt we can receive list of books and also short description of each book.

### 2. I would like to create a short list of 10 history books

Using such kind of prompt where we specify the word: short we can receive just list of books without description.

### 3. Give short list of 5 books and rate it

With such king of prompt we can also receive rating for each of the book without sorting.

## Configuration of Semantic Kernel

### When we have temperature 1.

We receive good answers with a short description of the book in case we do not specify the word short.

### When we have temperature 0.5.

It generates a descirption only when we ask about it, but here we start receiving a problem connected with the output 
it generates less than expected results.

### When we have temperature 0.1.

Context of generated answer still the same for 10 books it generates only 6 of them and use description only of highlighted.

### Setting .withResultsPerPrompt(any Number)

It still generates 10 books while I am not specifying a number of books I want to receive.

Nevertheless, which settings and types of book I ask in prompt, it still remembers the previous context and generates 10 books.

### Setting .withMaxTokens(75)

It tries to generate as much books as possible, so it might start typing the name of the books and due to the context limitation is not finishing.


