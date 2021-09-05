#include<stdio.h>
#include<cs50.h>
#include<ctype.h>
#include<string.h>

int main(int argc, string args[])
{
    if (argc != 2) //If command line arguments are not exactly 2
    {
        printf("Usage: ./substitution Key\n");
        return 1; //return false
    }

    int length = strlen(args[1]); //counting the no. of characters in the key

    if (length != 26) //key needs to be exactly 26 characters
    {
        printf("Key must contain 26 characters !\n");
        return 1;
    }

    for (int i = 0; i < length; i++) //checking for all characters in key must be an alphabet
    {
        if (!isalpha(args[1][i]))
        {
            printf("Usage: ./substitution Key\n");
            printf("Key must contain only alpabetic characters !\n");
            return 1;
        }
        args[1][i] = tolower(args[1][i]); //changing every character in key to lower case
    }

    for (int i = 0; i < length; i++)
    {
        for (int j = i + 1; j < length; j++)
        {
            if (args[1][i] == args[1][j]) //checking for repeating characters in the key
            {
                printf("Each charecter of the key must be unique !\n");
                return 1;
            }
        }
    }

    string text = get_string("plaintext: "); //get text from user
    int temp;
    int size = strlen(text); //text length

    //encripting the message
    for (int i = 0; i < size; i++)
    {
        if (isalpha(text[i])) //if it is alphabet
        {
            if (isupper(text[i])) //checking for case in plaintext to preserve the case
            {
                temp = text[i] - 65; //this ll give the position of the letter used for encrypting
                text[i] = toupper(args[1][temp]);
            }
            else
            {
                temp = text[i] - 97;
                text[i] = args[1][temp];
            }
        }
    }
    printf("ciphertext: %s\n", text); //displaying ciphertext
    return 0; //End the program
}