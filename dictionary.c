// Implements a dictionary's functionality

#include <stdbool.h>
#include <stdlib.h>
#include "dictionary.h"
#include <stdio.h>
#include <string.h>
#include <strings.h>
#include <ctype.h>

// Represents a node in a hash table
typedef struct node
{
    char word[LENGTH + 1];
    struct node *next;
}
node;

// Number of buckets in hash table
const unsigned int buckets = 5490;

//Number of words in dictionary
int wordCount = 0;

// Hash table
node *table[buckets];

// Returns true if word is in dictionary else false
bool check(const char *word)
{
    int len = strlen(word);
    char temp[len + 1];
    temp[len] = '\0';
    int i = 0;

    while(i < len)
    {
        temp[i] = *word;
        i++;
        word++;
    }

    int index = hash(temp);
    node *cursor = table[index];
    while (cursor != NULL)
    {
        if (strcasecmp(cursor->word, temp) == 0)
        {
            return true;
        }
        else
        {
            cursor = cursor->next;
        }
    }
    return false;
}

// Hashes word to a number
//Got it from https://www.geeksforgeeks.org/hash-function-for-string-data-in-c-sharp/
unsigned int hash(const char *word)
{
    int total = 0;
    int len = strlen(word);
    //Add the ASCII value of each character in a string
    for (int i = 0; i < len; i++)
    {
        total += (int)(tolower(word[i]));
    }
        return total;

}

// Loads dictionary into memory, returning true if successful else false
bool load(const char *dictionary)
{
    FILE *file = fopen(dictionary, "r");
    if (file == NULL)
    {
        fprintf(stderr, "Could not open %s.\n", dictionary);
        return false;
    }

    char dictionaryWord[LENGTH + 1];

    while (fscanf(file, "%s", dictionaryWord) != EOF)
    {
        node *newNode = malloc(sizeof(node));

        if (newNode == NULL)
        {
            fprintf(stderr, "Not enough memory available to load %s.\n", dictionary);
            return false;
        }

        strcpy(newNode->word, dictionaryWord);

        int index = hash(dictionaryWord);

        if (table[index] == NULL)
        {
            table[index] = newNode;
            newNode->next = NULL;
        }
        else
        {
            newNode->next = table[index];
            table[index] = newNode;
        }
        wordCount++;
    }
    fclose(file);
    return true;
}

// Returns number of words in dictionary if loaded else 0 if not yet loaded
unsigned int size(void)
{
    return wordCount;
}

// Unloads dictionary from memory, returning true if successful else false
bool unload(void)
{
    //iterate through every 'bucket'
    for (int i = 0; i < buckets; i++)
    {
        node *root = table[i]; // initially points to 1st node

        while(root != NULL)
        {
                node *tmp = root; // tmp points to root
                root = root -> next; // root points to next node
                free(tmp); // tmp frees the prior node
        }
   }

    return true;
}
