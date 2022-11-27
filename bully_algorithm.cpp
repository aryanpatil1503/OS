//============================================================================
// Name        : lp_bully_algorithm.cpp
// Author      : 
// Version     :
// Copyright   : Your copyright notice
// Description : Hello World in C++, Ansi-style
//============================================================================

#include <iostream>
#include<string.h>

using namespace std;

struct rr
{
char name[10];
int prior;
char state[10];
}proc[10];
int i,j,k,l,m,n,m1,m2;
int main()
{
cout<<"\n enter the number of proceess \t";
cin>>n;
for(i=0;i<n;i++)
{
cout<<"\nenter the name of process\t";
cin>>proc[i].name;
cout<<"\nenter the priority of process\t";
cin>>proc[i].prior;
strcpy(proc[i].state,"active");
}
for(i=0;i<n-1;i++)
{
for(j=0;j<n-1;j++)
{
if(proc[j].prior<proc[j+1].prior)
{
char ch[5];
int t=proc[j].prior;
proc[j].prior= proc[j+1].prior;
proc[j+1].prior=t;
strcpy(ch,proc[j].name);
strcpy(proc[j].name,proc[j+1].name);
strcpy(proc[j+1].name,ch);
}
}
}
int max=0;
for(i=0;i<n;i++)
cout<<"\n"<<proc[i].name<<"\t"<<proc[i].prior;
for(i=0;i<n;i++)
{
for(i=0;i<n;i++)
{
if(max<proc[i].prior)
max=proc[i].prior;
}
}
for(i=0;i<n;i++)
{
if(proc[i].prior==max)
{
cout<<"\nprocess "<<proc[i].name<<" select as coordinator";
strcpy(proc[i].state,"active");
break;
}
}
int pr;
while(1)
{
int ch;
cout<<"\n1) election\t";
cout<<"\n2) crash\t";
cout<<"\n3) active \t";
cout<<"\n4) display\t";
cout<<"\n5) quit\t";
cin>>ch;
int max1=0;
int ar[20];
k=0;
int fl=0;
switch(ch)
{
case 1: char str[5];
 cout<<"\n 1)intialise election\t";
 cin>>str;
 fl=0;
 l1: for(i=0;i<n;i++)
 {
	 if(strcmp(str,proc[i].name)==0)
	 {
		 pr=proc[i].prior;
	 }
 }
 //cout<<"\n"<<pr;
 for(i=0;i<n;i++)
 {
	 if(pr<proc[i].prior)
	 {
		 cout<<"\nprocess "<<str<<" send message to "<<proc[i].name;
	 }
 }
 for(i=0;i<n;i++)
 {
	 if(pr<proc[i].prior && strcmp(proc[i].state,"active")==0 )
	 {
		 if(fl==0)
		 {
			 ar[k]= proc[i].prior;
			 k++;
		 }
		 cout<<"\nprocess "<<proc[i].name<<" send OK message to "<<str;
		 if(proc[i].prior>max1)
			 max1=proc[i].prior;
	 }
 }
 fl=1;
 if(k!=0)
 {
 k=k-1;
 for(i=0;i<n;i++)
 {
	 if(ar[k]==proc[i].prior)
		 strcpy(str,proc[i].name);
 }

 goto l1;
 }
 m=0;
 for(j=0;j<n;j++)
 {
	 if(proc[j].prior>m && strcmp(proc[j].state,"active")==0 )
	 {
		 cout<<"\nprocess "<<proc[j].name <<" is select as new coordinator";
		 strcpy(proc[j].state,"inactive");
		 break;
	 }
 }
 for(i=0;i<n;i++)
 {
	 if(strcmp(proc[i].state,"active")==0 && proc[j].prior>proc[i].prior)
	 {
		 cout<<"\nprocess "<<proc[j].name<<" send alert message to "<<proc[i].name;
	 }
 }
 break;
case 2:
	cout<<"\ncrash element\t";
	cin>>str;
	rr a1;
	for(i=0;i<n;i++)
	{
		 if(strcmp(str,proc[i].name)==0)
		 {
			 pr=proc[i].prior;
			 a1=proc[i];
			 strcpy(proc[i].state,"inactive");
			 cout<<proc[i].name<<" ";
			 cout<<proc[i].state<<endl;
		 }
	}
	m1=0;
	for(i=0;i<n;i++)
	{
	for(i=0;i<n;i++)
	{
	if(m1<proc[i].prior)
	m1=proc[i].prior;
	}
	}
	/*if(m1=!pr){
		exit(1);
	}*/
	if(m1==pr){
		cout<<"\n intialize election\t";
		 cin>>str;
		fl=0;
		 l3: for(i=0;i<n;i++)
		 {
			 if(strcmp(str,proc[i].name)==0)
			 {
				 pr=proc[i].prior;
			 }
		 }
		 //cout<<"\n"<<pr;
		 for(i=0;i<n;i++)
		 {
			 if(pr<proc[i].prior)
			 {
				 cout<<"\nprocess "<<str<<" send message to "<<proc[i].name;
			 }
		 }
		 for(i=0;i<n;i++)
		 {
			 if(pr<proc[i].prior && strcmp(proc[i].state,"active")==0 )
			 {
				 if(fl==0)
				 {
					 ar[k]= proc[i].prior;
					 k++;
				 }
				 cout<<"\nprocess "<<proc[i].name<<" send OK message to "<<str;
				 if(proc[i].prior>m1)
					 max1=proc[i].prior;
			 }
		 }
		 fl=1;
		 if(k!=0)
		 {
		 k=k-1;
		 for(i=0;i<n;i++)
		 {
			 if(ar[k]==proc[i].prior)
				 strcpy(str,proc[i].name);
		 }

		 goto l3;
		 }
		 m=0;
		 for(j=0;j<n;j++)
		 {
			 if(proc[j].prior>m && strcmp(proc[j].state,"active")==0 )
			 {
				 cout<<"\nprocess "<<proc[j].name <<" is select as new coordinator";
				 strcpy(proc[j].state,"active");
				 break;
			 }
		 }
		 for(i=0;i<n;i++)
		 {
			 if(strcmp(proc[i].state,"active")==0 && proc[j].prior>proc[i].prior)
			 {
				 cout<<"\nprocess "<<proc[j].name<<" send alert message to "<<proc[i].name;
			 }
		 }

	}



	break;


case 3:cout<<"\nactive element\t";
cin>>str;
rr a;
for(i=0;i<n;i++)
{
	 if(strcmp(str,proc[i].name)==0)
	 {
		 pr=proc[i].prior;
		 a=proc[i];
		 strcpy(proc[i].state,"active");
		 cout<<proc[i].name<<endl;
		 cout<<proc[i].state<<endl;
	 }
}
m2=0;
for(i=0;i<n;i++)
{
for(i=0;i<n;i++)
{
if(m2<proc[i].prior)
m2=proc[i].prior;
}
}
if (m2==pr){
	cout<<"\nprocedure "<<a.name<<" is coordinator";
}
else{
	fl=0;
	 l2: for(i=0;i<n;i++)
	 {
		 if(strcmp(str,proc[i].name)==0)
		 {
			 pr=proc[i].prior;
		 }
	 }
	 //cout<<"\n"<<pr;
	 for(i=0;i<n;i++)
	 {
		 if(pr<proc[i].prior)
		 {
			 cout<<"\nprocess "<<str<<" send message to "<<proc[i].name;
		 }
	 }
	 for(i=0;i<n;i++)
	 {
		 if(pr<proc[i].prior && strcmp(proc[i].state,"active")==0 )
		 {
			 if(fl==0)
			 {
				 ar[k]= proc[i].prior;
				 k++;
			 }
			 cout<<"\nprocess "<<proc[i].name<<" send OK message to "<<str;
			 if(proc[i].prior>max1)
				 max1=proc[i].prior;
		 }
	 }
	 fl=1;
	 if(k!=0)
	 {
	 k=k-1;
	 for(i=0;i<n;i++)
	 {
		 if(ar[k]==proc[i].prior)
			 strcpy(str,proc[i].name);
	 }

	 goto l2;
	 }
	 m=0;
	 for(j=0;j<n;j++)
	 {
		 if(proc[j].prior>m && strcmp(proc[j].state,"active")==0 )
		 {
			 cout<<"\nprocess "<<proc[j].name <<" is select as new coordinator";
			 strcpy(proc[j].state,"active");
			 break;
		 }
	 }
	 for(i=0;i<n;i++)
	 {
		 if(strcmp(proc[i].state,"active")==0 && proc[j].prior>proc[i].prior)
		 {
			 cout<<"\nprocess "<<proc[j].name<<" send alert message to "<<proc[i].name;
		 }
	 }

}
break;
break;
case 4:for(int y=0;y<n;y++){
	cout<<proc[y].name<<" "<<proc[y].prior<<" "<<proc[y].state<<endl;
}
break;
case 5:exit(1);
}
}
return 0;}

