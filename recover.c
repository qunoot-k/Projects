#include <stdio.h>
#include <stdint.h>

typedef uint8_t BYTE;
int main(int argc, char *argv[])
{
    //check proper usage
    if (argc != 2)
    {
        printf("Usage: ./recover image\n");
        return 1;
    }

    //open stream to read from file
    FILE *inFile = fopen(argv[1], "r");

    //If file does not exist
    if (!inFile)
    {
        printf("Could not open file!\n");
        return 1;
    }

    BYTE buffer[512];
    FILE *outFile = NULL;
    int imageNum = 0;
    char fileName[8];

    //Read until end of file
    while (fread(buffer, sizeof(buffer), 1, inFile))
    {
        //check if file is .jpg
        //Check if its the starting of a new file
        if (buffer[0] == 0xff && buffer[1] == 0xd8 && buffer[2] == 0xff && (buffer[3] & 0xf0) == 0xe0)
        {
            //close the old file
            if (imageNum > 0)
            {
                fclose(outFile);
            }
            sprintf(fileName, "%03i.jpg", imageNum);
            outFile = fopen(fileName, "w");
            imageNum++;
        }
        //Write to the new file
        if (outFile != NULL)
        {
            fwrite(buffer, sizeof(buffer), 1, outFile);
        }
    }

    //Close input and output stream to the file
    fclose(outFile);
    fclose(inFile);
    return 0;
}
