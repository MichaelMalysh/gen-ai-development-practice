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

## Module 2 Exploring Section

### temperature and top_p
When we set either temperature or top_p parameter with the same value we get same results.
Wxploring combination of both params like temperature and top_p putting at once we can see that the creativity level is a middle of both values set. 

### stop sequence
When we set stop sequence we can see that the model is generating a sequence without a problem before the stop word appeared and it stops imidiately when it appears in the output.

### chat history
Using chat history instance as a singleton we can control the whole history in one session, so for each of user we can controll and store the whole conversation and manage it.

## Module 3

For the configuration:
```json
{
  "prompt": "I want to find top-10 books",
  "max_tokens": 100,
  "temperature": 1.0,
  "model": "gpt-4",
  "stop_words": ["book"],
  "results_per_prompt": 3
}
```
I will receive the answer:
```text
Certainly! Please specify the genre or type of books you're interested in so I can provide a more tailored top-10 list for you. Are you looking for fiction, non-fiction, thriller, romance, science fiction, or something else?
```

In case I would increase max words allowed to 1000 the answer would be:
```text
Here is a list of top 10 widely acclaimed books across various genres:

1. **"To Kill a Mockingbird" by Harper Lee** - A classic American novel about innocence, strength, and morality.
2. **"1984" by George Orwell** - A dystopian novel on totalitarian governance and surveillance.
3. **"Pride and Prejudice" by Jane Austen** - A romantic novel that also provides a satire of its time.
4. **"The Catcher in the Rye" by J.D. Salinger** - An exploration of teenage angst and alienation.
5. **"The Great Gatsby" by F. Scott Fitzgerald** - A critique of the American Dream in the roaring 1920s.
6. **"Harry Potter Series by J.K. Rowling** - A popular fantasy series following the life of a young wizard, Harry Potter.
7. **"The Lord of the Rings" by J.R.R. Tolkien** - A high-fantasy epic adventure.
8. **"Sapiens: A Brief History of Humankind" by Yuval Noah Harari** - An exploration of the evolution and impact of Homo sapiens.
9. **"Beloved" by Toni Morrison** - A profound
```

For the configuration: 
```json
{
  "prompt": "I want to find top-10 books",
  "max_tokens": 1000,
  "temperature": 0.1,
  "model": "gpt-35-turbo",
  "stop_words": ["book"],
  "results_per_prompt": 3
}
```

I will receive the answer:
```text
Sure! Could you please specify the genre or topic you are interested in?
```

For the configuration:
```json

```