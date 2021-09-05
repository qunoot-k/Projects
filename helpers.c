#include "helpers.h"
#include <math.h>

// Convert image to grayscale
void grayscale(int height, int width, RGBTRIPLE image[height][width])
{
    int avg;
    for (int i = 0; i < height; i++)
    {
        for (int j = 0; j < width; j++)
        {
            avg = round((image[i][j].rgbtBlue + image[i][j].rgbtGreen + image[i][j].rgbtRed) / 3.0);
            image[i][j].rgbtBlue = avg;
            image[i][j].rgbtGreen = avg;
            image[i][j].rgbtRed = avg;
        }
    }
}

// Convert image to sepia
void sepia(int height, int width, RGBTRIPLE image[height][width])
{
    for (int i = 0; i < height; i++)
    {
        for (int j = 0; j < width; j++)
        {
            float sepiaBlue = round(0.272 * image[i][j].rgbtRed + 0.534 * image[i][j].rgbtGreen + 0.131 * image[i][j].rgbtBlue);
            float sepiaGreen = round(0.349 * image[i][j].rgbtRed + 0.686 * image[i][j].rgbtGreen + 0.168 * image[i][j].rgbtBlue);
            float sepiaRed = round(0.393 * image[i][j].rgbtRed + 0.769 * image[i][j].rgbtGreen + 0.189 * image[i][j].rgbtBlue);
            if (sepiaBlue > 255)
            {
                sepiaBlue = 255;
            }
            if (sepiaGreen > 255)
            {
                sepiaGreen = 255;
            }
            if (sepiaRed > 255)
            {
                sepiaRed = 255;
            }
            image[i][j].rgbtBlue = sepiaBlue;
            image[i][j].rgbtGreen = sepiaGreen;
            image[i][j].rgbtRed = sepiaRed;
        }
    }
}

// Reflect image horizontally
void reflect(int height, int width, RGBTRIPLE image[height][width])
{
    RGBTRIPLE temp;
    for (int i = 0; i < height; i++)
    {
        for (int j = 0, k = width - 1; j < width / 2; j++, k--)
        {
            temp = image[i][j];
            image[i][j] = image[i][k];
            image[i][k] = temp;
        }
    }
    return;
}

// Blur image
void blur(int height, int width, RGBTRIPLE image[height][width])
{
    float avgRed, avgGreen, avgBlue;
    int pxCount;

    //Creating a copy of image in order to use the orignal values to calculate average
    RGBTRIPLE duplicate[height][width];

    for (int i = 0; i < height; i++)
    {
        for (int j = 0; j < width; j++)
        {
            duplicate[i][j] = image[i][j];
        }
    }
    for (int row = 0; row < height; row++)
    {
        for (int column = 0; column < width; column++)
        {
            avgRed = 0, avgBlue = 0;
            avgGreen = 0, pxCount = 0;

            //dimensions for the smaller matrix
            //these will be used if the pixel is not in a corner or a side
            int innerRow = -1, innerCol = -1, lastRow = 1, lastCol = 1;

            /*-1 means position of pixel before current pixel
            0 is the pixel in focus
            1 is pos of pixel after the current pixel*/

            if (row == 0) //(1)
            {
                innerRow = 0;
            }
            else if (row == height - 1) //(2)
            {
                lastRow = 0;
            }

            if (column == 0) //(3)
            {
                innerCol = 0;
            }
            else if (column == width - 1) //(4)
            {
                lastCol = 0;
            }

            /* combnation of conditions (1) & (3), (1) & (4), (2) & (3), (2) & (4) are corner pixel
            only condition (1) or (2) or (3) or (4) are side pixels
            if none of the conditions met then it is a pixel somewhere in between*/

            //looping though the inner matrix
            for (int i = innerRow; i <= lastRow; i++)
            {
                for (int j = innerCol; j <= lastCol; j++)
                {
                    int ipx = i + row;
                    int jpx = j + column;
                    avgRed += duplicate[ipx][jpx].rgbtRed;
                    avgBlue += duplicate[ipx][jpx].rgbtBlue;
                    avgGreen += duplicate[ipx][jpx].rgbtGreen;
                    pxCount++;
                }
            }

            image[row][column].rgbtRed = round(avgRed / pxCount);
            image[row][column].rgbtBlue = round(avgBlue / pxCount);
            image[row][column].rgbtGreen = round(avgGreen / pxCount);
        }
    }
    return;
}
