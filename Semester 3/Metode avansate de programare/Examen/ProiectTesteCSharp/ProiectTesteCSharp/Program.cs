using System;
class A
{
 protected int x = 1;
 public A()
 {
 Print();
 Console.Write(x);
 }
 public virtual void Print()
 {
 Console.Write("A");
 }
}
class B : A
{
 protected new int x = 2;
 public B()
 {
 Print();
 Console.Write(x);
 }
 public override void Print()
 {
 Console.Write("B");
 }
}
class Program
{
 static void Main()
 {
 new B();
 }
}