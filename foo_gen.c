#include <stdio.h>
#define read(x) scanf("%d\n", &x)
#define write(x) printf("%d\n", x)
// function foo
void cs512foo() {
	int cs512a;
	read(cs512a);
	write(cs512a*223);
	write("hey ho lets go !!");
}

int main() {
	cs512foo();
}
