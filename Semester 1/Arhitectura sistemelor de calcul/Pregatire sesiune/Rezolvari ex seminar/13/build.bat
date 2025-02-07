nasm -fobj main.asm
nasm -fobj b.asm
alink main.obj b.obj -oPE -subsys console -entry start
