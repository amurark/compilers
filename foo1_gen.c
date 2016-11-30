#include <stdio.h>
#define read(x) scanf("%d",&x)
#define write(x) printf("%d\n",x)
#define print(x) printf(x)

int GLOBAL[16];
int GLOBAL1[16];
int GLOBAL2[16];
int GLOBAL3[16];

void populate_arrays(void)
{
    GLOBAL[0] = 0; GLOBAL1[0] = 15; GLOBAL2[0] = 5; GLOBAL3[0] = 13;
    GLOBAL[1] = 1; GLOBAL1[1] = 14; GLOBAL2[1] = 5; GLOBAL3[1] = 9;
    GLOBAL[2] = 2; GLOBAL1[2] = 13; GLOBAL2[2] = 5; GLOBAL3[2] = 12;
    GLOBAL[3] = 3; GLOBAL1[3] = 12; GLOBAL2[3] = 5; GLOBAL3[3] = 1;
    GLOBAL[4] = 4; GLOBAL1[4] = 11; GLOBAL2[4] = 5; GLOBAL3[4] = 0;
    GLOBAL[5] = 5; GLOBAL1[5] = 10; GLOBAL2[5] = 5; GLOBAL3[5] = 14;
    GLOBAL[6] = 6; GLOBAL1[6] = 9; GLOBAL2[6] = 5; GLOBAL3[6] = 3;
    GLOBAL[7] = 7; GLOBAL1[7] = 8; GLOBAL2[7] = 5; GLOBAL3[7] = 2;
    GLOBAL[8] = 8; GLOBAL1[8] = 7; GLOBAL2[8] = 5; GLOBAL3[8] = 11;
    GLOBAL[9] = 9; GLOBAL1[9] = 6; GLOBAL2[9] = 5; GLOBAL3[9] = 8;
    GLOBAL[10] = 10; GLOBAL1[10] = 5; GLOBAL2[10] = 5; GLOBAL3[10] = 6;
    GLOBAL[11] = 11; GLOBAL1[11] = 4; GLOBAL2[11] = 5; GLOBAL3[11] = 4;
    GLOBAL[12] = 12; GLOBAL1[12] = 3; GLOBAL2[12] = 5; GLOBAL3[12] = 5;
    GLOBAL[13] = 13; GLOBAL1[13] = 2; GLOBAL2[13] = 5; GLOBAL3[13] = 10;
    GLOBAL[14] = 14; GLOBAL1[14] = 1; GLOBAL2[14] = 5; GLOBAL3[14] = 7;
    GLOBAL[15] = 15; GLOBAL1[15] = 0; GLOBAL2[15] = 5; GLOBAL3[15] = 15;
}

void print_arrays(void)
{
    int LOCAL[10];
    LOCAL[1] = 16;
    print("Array_1:\n");
    LOCAL[0] = 0;
    C1:;
if ( LOCAL[0]< LOCAL[1])
    goto C2;
  goto C3;
   C2:;
LOCAL[2] = GLOBAL[LOCAL[0]];
	write(LOCAL[2]);
LOCAL[3] = LOCAL[0]+1;
	LOCAL[0] = LOCAL[3];
    goto C1;
C3:;

    print("\nArray_2:\n");
    LOCAL[0] = 0;
    C4:;
if ( LOCAL[0]< LOCAL[1])
    goto C5;
  goto C6;
   C5:;
LOCAL[4] = GLOBAL1[LOCAL[0]];
	write(LOCAL[4]);
LOCAL[5] = LOCAL[0]+1;
	LOCAL[0] = LOCAL[5];
    goto C4;
C6:;

    print("\nArray_3:\n");
    LOCAL[0] = 0;
    C7:;
if ( LOCAL[0]< LOCAL[1])
    goto C8;
  goto C9;
   C8:;
LOCAL[6] = GLOBAL2[LOCAL[0]];
	write(LOCAL[6]);
LOCAL[7] = LOCAL[0]+1;
	LOCAL[0] = LOCAL[7];
    goto C7;
C9:;

    print("\nArray_4:\n");
    LOCAL[0] = 0;
    C10:;
if ( LOCAL[0]< LOCAL[1])
    goto C11;
  goto C12;
   C11:;
LOCAL[8] = GLOBAL3[LOCAL[0]];
	write(LOCAL[8]);
LOCAL[9] = LOCAL[0]+1;
	LOCAL[0] = LOCAL[9];
    goto C10;
C12:;
    print("\n");
}

int main()
{
    int LOCAL[39];
    LOCAL[1] = 16;

    populate_arrays();
    print_arrays();

    LOCAL[1] = 16;

    LOCAL[0] = 0;
    LOCAL[3] = LOCAL[1]-1;
    C13:;
if ( LOCAL[0]< LOCAL[3])
    goto C14;
  goto C15;
   C14:;
LOCAL[5] = LOCAL[0]+1;
LOCAL[6] = GLOBAL[LOCAL[5]];
LOCAL[4] = GLOBAL[LOCAL[0]];
	if (LOCAL[4]> LOCAL[6])
	goto C16;
    goto C17;
     C16:;
LOCAL[7] = GLOBAL[LOCAL[0]];
	    LOCAL[2] = LOCAL[7];
LOCAL[8] = LOCAL[0]+1;
LOCAL[9] = GLOBAL[LOCAL[8]];
	    GLOBAL[LOCAL[0]] = LOCAL[9];
LOCAL[10] = LOCAL[0]+1;
	    GLOBAL[LOCAL[10]] = LOCAL[2];
	    LOCAL[0] = 0;
	    goto C13;
	C17:;

LOCAL[11] = LOCAL[0]+1;
	LOCAL[0] = LOCAL[11];
    goto C13;
C15:;

    LOCAL[0] = 0;
    LOCAL[12] = LOCAL[1]-1;
    C18:;
if ( LOCAL[0]< LOCAL[12])
    goto C19;
  goto C20;
   C19:;
LOCAL[14] = LOCAL[0]+1;
LOCAL[15] = GLOBAL1[LOCAL[14]];
LOCAL[13] = GLOBAL1[LOCAL[0]];
	if (LOCAL[13]> LOCAL[15])
	goto C21;
    goto C22;
     C21:;
LOCAL[16] = GLOBAL1[LOCAL[0]];
	    LOCAL[2] = LOCAL[16];
LOCAL[17] = LOCAL[0]+1;
LOCAL[18] = GLOBAL1[LOCAL[17]];
	    GLOBAL1[LOCAL[0]] = LOCAL[18];
LOCAL[19] = LOCAL[0]+1;
	    GLOBAL1[LOCAL[19]] = LOCAL[2];
	    LOCAL[0] = 0;
	    goto C18;
	C22:;

LOCAL[20] = LOCAL[0]+1;
	LOCAL[0] = LOCAL[20];
    goto C18;
C20:;


    LOCAL[0] = 0;
    LOCAL[21] = LOCAL[1]-1;
    C23:;
if ( LOCAL[0]< LOCAL[21])
    goto C24;
  goto C25;
   C24:;
LOCAL[23] = LOCAL[0]+1;
LOCAL[24] = GLOBAL2[LOCAL[23]];
LOCAL[22] = GLOBAL2[LOCAL[0]];
	if (LOCAL[22]> LOCAL[24])
	goto C26;
    goto C27;
     C26:;
LOCAL[25] = GLOBAL[LOCAL[0]];
	    LOCAL[2] = LOCAL[25];
LOCAL[26] = LOCAL[0]+1;
LOCAL[27] = GLOBAL2[LOCAL[26]];
	    GLOBAL2[LOCAL[0]] = LOCAL[27];
LOCAL[28] = LOCAL[0]+1;
	    GLOBAL2[LOCAL[28]] = LOCAL[2];
	    LOCAL[0] = 0;
	    goto C23;
	C27:;

LOCAL[29] = LOCAL[0]+1;
	LOCAL[0] = LOCAL[29];
    goto C23;
C25:;

    LOCAL[0] = 0;
    LOCAL[30] = LOCAL[1]-1;
    C28:;
if ( LOCAL[0]< LOCAL[30])
    goto C29;
  goto C30;
   C29:;
LOCAL[32] = LOCAL[0]+1;
LOCAL[33] = GLOBAL3[LOCAL[32]];
LOCAL[31] = GLOBAL3[LOCAL[0]];
	if (LOCAL[31]> LOCAL[33])
	goto C31;
    goto C32;
     C31:;
LOCAL[34] = GLOBAL3[LOCAL[0]];
	    LOCAL[2] = LOCAL[34];
LOCAL[35] = LOCAL[0]+1;
LOCAL[36] = GLOBAL3[LOCAL[35]];
	    GLOBAL3[LOCAL[0]] = LOCAL[36];
LOCAL[37] = LOCAL[0]+1;
	    GLOBAL3[LOCAL[37]] = LOCAL[2];
	    LOCAL[0] = 0;
	    goto C28;
	C32:;

LOCAL[38] = LOCAL[0]+1;
	LOCAL[0] = LOCAL[38];
    goto C28;
C30:;

    print_arrays();
}
