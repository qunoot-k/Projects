Morse Code <br />
This is a java program for an automatic decoder on the assumption that the raw input data has been preprocessed. In this preprocessed form, dots and dashes are represented by two-bit sequences, as indication of end of letter and end of word. <br />
The end of a sentence is denoted by two consecutive end of word sequences, i.e., 1111. The data for the program will be therefore be entered as a sequence of bit pairs. <br />
For example, the message 
HELLO STOP I LOVE YOU STOP <br />
will be represented by the bit sequence 
0101010100010001100101000110010100101010001111010100110110010100101010000101011000010011100110100010101000010110001111<br />
In order to reduce searching time, the alphabetic characters are stored using B Tree 
