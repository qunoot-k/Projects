#include <cs50.h>
#include <stdio.h>
#include <string.h>

// Max number of candidates
#define MAX 9

// preferences[i][j] is number of voters who prefer i over j
int preferences[MAX][MAX];

// locked[i][j] means i is locked in over j
bool locked[MAX][MAX];

// Each pair has a winner, loser
typedef struct
{
    int winner;
    int loser;
}
pair;

// Array of candidates
string candidates[MAX];
pair pairs[MAX * (MAX - 1) / 2];

int pair_count;
int candidate_count;

// Function prototypes
bool vote(int rank, string name, int ranks[]);
void record_preferences(int ranks[]);
void add_pairs(void);
void sort_pairs(void);
void lock_pairs(void);
void print_winner(void);

int main(int argc, string argv[])
{
    // Check for invalid usage
    if (argc < 2)
    {
        printf("Usage: tideman [candidate ...]\n");
        return 1;
    }

    // Populate array of candidates
    candidate_count = argc - 1;
    if (candidate_count > MAX)
    {
        printf("Maximum number of candidates is %i\n", MAX);
        return 2;
    }
    for (int i = 0; i < candidate_count; i++)
    {
        candidates[i] = argv[i + 1];
    }

    // Clear graph of locked in pairs
    for (int i = 0; i < candidate_count; i++)
    {
        for (int j = 0; j < candidate_count; j++)
        {
            locked[i][j] = false;
        }
    }

    pair_count = 0;
    int voter_count = get_int("Number of voters: ");

    // Query for votes
    for (int i = 0; i < voter_count; i++)
    {
        // ranks[i] is voter's ith preference
        int ranks[candidate_count];

        // Query for each rank
        for (int j = 0; j < candidate_count; j++)
        {
            string name = get_string("Rank %i: ", j + 1);

            if (!vote(j, name, ranks))
            {
                printf("Invalid vote.\n");
                return 3;
            }
        }

        record_preferences(ranks);

        printf("\n");
    }

    add_pairs();
    sort_pairs();
    lock_pairs();
    print_winner();
    return 0;
}

// Update ranks given a new vote
bool vote(int rank, string name, int ranks[])
{
    for (int i = 0; i < candidate_count; i++)
    {
        if (strcmp(name, candidates[i]) == 0) //check if name eneterd by voter is a candidate
        {
            ranks[rank] = i; //set candidate according to preferred ranks in ranks array
            return true;
        }
    }
    return false; //No such candidate exists
}

// Update preferences given one voter's ranks
void record_preferences(int ranks[])
{
    for (int i = 0; i < candidate_count; i++)
    {
        int currentCandidate = ranks[i]; //preffered candidate x
        for (int j = i + 1; j < candidate_count; j++)
        {
            preferences[currentCandidate][ranks[j]]++; //update candidate x preffered over the rest in array
        }
    }
    return;
}

// Record pairs of candidates where one is preferred over the other
void add_pairs(void)
{
    for (int i = 0; i < candidate_count; i++)
    {
        //array checks diagonally otherwise a pair'll be recorded twice
        for (int j = i + 1; j < candidate_count; j++)
        {
            if (preferences[i][j] > preferences[j][i]) //x preffered over y
            {
                pairs[pair_count].winner = i;
                pairs[pair_count].loser = j;
                pair_count++; //counting no. of pairs
            }
            else if (preferences[i][j] < preferences[j][i]) //y preffered over x
            {
                pairs[pair_count].winner = j;
                pairs[pair_count].loser = i;
                pair_count++;
            }
        }
    }
    return;
}

void swapPair(pair *xpair, pair *ypair)
{
    pair temp = *xpair;
    *xpair = *ypair;
    *ypair = temp;
}

void swapStrength(int *xstrength, int *ystrength)
{
    int temp = *xstrength;
    *xstrength = *ystrength;
    *ystrength = temp;
}

// Sort pairs in decreasing order by strength of victory
void sort_pairs(void)
{
    int strength[pair_count]; //record strength no. Voters prefer winner - no. of voters prefer loser in a pair
    for (int i = 0; i < pair_count; i++)
    {
        strength[i] = preferences[pairs[i].winner][pairs[i].loser] - preferences[pairs[i].loser][pairs[i].winner];
    }

    //selection sort
    for (int i = 0; i < pair_count - 1; i++)
    {
        int pos = i;
        for (int j = i + 1; j < pair_count; j++)
        {
            if (strength[j] > strength[pos])
            {
                //find max strength
                pos = j;  //record the pos of max strength
            }
        }
        if (pos != i)
        {
            swapPair(&pairs[pos], &pairs[i]);  //swap pairs
            swapStrength(&strength[pos], &strength[i]);  //swap strength
        }
    }
}

// Lock pairs into the candidate graph in order, without creating cycles
void lock_pairs(void)
{
    for (int i = 0; i < pair_count; i++)
    {
        int currentloser = pairs[i].loser;
        bool cycle = false;
        for (int j = 0; j < pair_count; j++)
        {
            //if the currentloser in a pair has already been a winner
            //the according to strength of sorted pairs, do not lock the current pair
            if (locked[currentloser][j] == true)
            {
                cycle = true;
                break;
            }
        }
        if (cycle == false) //if the loser has not been a winner before
        {
            locked[pairs[i].winner][currentloser] = true; //lock the pairs
        }
    }
    return;
}

// Print the winner of the election
void print_winner(void)
{
    for (int i = 0; i < candidate_count; i++)
    {
        for (int j = 0; j < candidate_count; j++)
        {
            //column of the winner will be completely false in locked pairs
            if (locked[j][i] == false)
            {
                if (j == candidate_count - 1) //reached the end of the colmn
                {
                    printf("%s\n", candidates[i]);
                }
            }
            else //if a column contains true then they have been a loser at least once, hence exit the current candidate
            {
                break;
            }
        }
    }
    return;
}
