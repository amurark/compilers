#include <stdio.h>
#define read(x) scanf("%d",&x)
#define write(x) printf("%d\n",x)

int sub(int m, int n, int k);

int add(int a, int c) {
  int b;
  if(a == 2) {
	read(b);
  }
  return a+b;
}

int main(int x, int y) {
  int a, b;
  read(a);
  write(add(a));
}
