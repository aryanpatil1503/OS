



//============================================================================
// Name        : lp_ring_algorithm.cpp
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
int index;
int id;
int f;
char state[10];
}proc[10];
int i,j,k,m,n;
int main()
{
int temp;
char str[10];
cout<<"\n enter the number of process\t";
cin>>n;
for(i=0;i<n;i++)
{
proc[i].index;
cout<<"\n enter priority of process\t";
cin>>proc[i].id;
strcpy(proc[i].state,"active");
proc[i].f=0;
}
// sorting
for(i=0;i<n-1;i++)
{
for(j=0;j<n-1;j++)
{
if(proc[j].id>proc[j+1].id)
{
temp=proc[j].id;
proc[j].id=proc[j+1].id;
proc[j+1].id=temp;
}
}
}
for(i=0;i<n;i++){
	cout<<i<<' '<<proc[i].id<<endl;
}
//printf("[%d] %d\t",i,proc[i].id);
int init;
int ch;
int temp1;
int temp2;
int arr[10];
int max;
int crash;
int active;
strcpy(proc[n-1].state,"active");
cout<<"\nprocess "<<proc[n-1].id<<" select as coordinator";
while(1)
{
cout<<"\n1)election\n2)crash\n3)active \n4)display\n5)quit\n";
cin>>ch;
for(i=0;i<n;i++)
{
	proc[i].f=0;
}
switch(ch)
{
case 1:
cout<<"\nenter the process Number who intialised election";
cin>>init;
 temp2=init;
 temp1=init+1;
 i=0;
 while(temp2!=temp1)
 {
 if(strcmp(proc[temp1-1].state,"active")==0 && proc[temp1-1].f==0 )
 {
 cout<<"\n process "<<proc[init-1].id<<" send message to "<<proc[temp1-1].id<<"\n";
 proc[temp1-1].f=1;
 arr[i]=proc[init-1].id;
 init=temp1;


 i++;
 cout<<"active array"<<" ";
 for(int j=0;j<i;j++){
	 cout<<arr[j]<<" ";
 }
 }

 if(temp1-1==n)
 temp1=0;
 else
 temp1++;
 }
 cout<<"\n process "<<proc[init-1].id<<" send message to "<<proc[temp1-1].id<<"\n";
 arr[i]=proc[init-1].id;
 for(int j=0;j<=i;j++){
	 cout<<arr[j]<<" ";
 }
 i++;
 max=-1;
 for(j=0;j<i;j++)
 {
 if(max<arr[j])
 max=arr[j];
 }
 cout<<"\nprocess "<<max<<" select as coordinator";

 for(i=0;i<n;i++)
 {
 if(proc[i].id==max)
 {
 strcpy(proc[i].state,"active");
// cout<<"\n"<<i<<" "<<proc[i].id<<"deactivate\n";
 }}
 break;

case 5:
	exit(1);
	break;
case 4:
	for(int y=0;y<n;y++){
		cout<<y<<" "<<proc[y].id<<" "<<" "<<proc[y].state<<endl;
	}
	break;
case 2:
	cout<<"\nenter the process Number who crashed";
	cin>>crash;
	for(int b=0;b<n;b++){
		if(proc[b].id==crash){
			strcpy(proc[b].state,"inactive");

		}
	}
	max=-1;
	for(int b=0;b<n;b++)
	 {
	 if(max<proc[b].id){
	 max=proc[b].id;
	 }
	}
	if(max==crash){
		cout<<"\nenter the process Number who intialised election";
		cin>>init;
		 temp2=init;
		 temp1=init+1;
		 i=0;
		 while(temp2!=temp1)
		 {
		 if(strcmp(proc[temp1-1].state,"active")==0 && proc[temp1-1].f==0 )
		 {
		 cout<<"\n process "<<proc[init-1].id<<" send message to "<<proc[temp1-1].id<<"\n";
		 proc[temp1-1].f=1;
		 arr[i]=proc[init-1].id;
		 init=temp1;


		 i++;
		 cout<<"active array"<<" ";
		 for(int j=0;j<i;j++){
			 cout<<arr[j]<<" ";
		 }
		 }

		 if(temp1-1==n)
		 temp1=0;
		 else
		 temp1++;
		 }
		 cout<<"\n process "<<proc[init-1].id<<" send message to "<<proc[temp1-1].id<<"\n";
		 arr[i]=proc[init-1].id;
		 for(int j=0;j<=i;j++){
			 cout<<arr[j]<<" ";
		 }
		 i++;
		 max=-1;
		 for(j=0;j<i;j++)
		 {
		 if(max<arr[j])
		 max=arr[j];
		 }
		 cout<<"\nprocess "<<max<<" select as coordinator";

		 for(i=0;i<n;i++)
		 {
		 if(proc[i].id==max)
		 {
		 strcpy(proc[i].state,"active");
		// cout<<"\n"<<i<<" "<<proc[i].id<<"deactivate\n";
		 }}
			 temp2=init;
		 			 temp1=init+1;
		 while(temp2!=temp1)
		 			 {
		 			 if(strcmp(proc[temp1-1].state,"active")==0 )
		 			 {
		 			 cout<<"\n process "<<proc[init-1].id<<" send message coordinator:"<<max<<" to process"<<proc[temp1-1].id<<"\n";

		 			 init=temp1;


		 			 i++;

		 			 }

		 			 if(temp1-1==n)
		 			 temp1=0;
		 			 else
		 			 temp1++;
		 			 }
	}
	break;
case 3:
	cout<<"\nenter the process Number who activate";
		cin>>active;
		for(int b=0;b<n;b++){
			if(proc[b].id==active){
				strcpy(proc[b].state,"active");

			}
		}
		max=-1;
		for(int b=0;b<n;b++)
		 {
		 if(max<proc[b].id){
		 max=proc[b].id;
		 }
		}
		if(max==active){
			cout<<"process "<<max<<"is selected as coordinator";
		}
		else{
			init=active;
			 temp2=init;
			 temp1=init+1;
			 i=0;
			 while(temp2!=temp1)
			 {
			 if(strcmp(proc[temp1-1].state,"active")==0 && proc[temp1-1].f==0 )
			 {
			 cout<<"\n process "<<proc[init-1].id<<" send message to "<<proc[temp1-1].id<<"\n";
			 proc[temp1-1].f=1;
			 arr[i]=proc[init-1].id;
			 init=temp1;


			 i++;
			 cout<<"active array"<<" ";
			 for(int j=0;j<i;j++){
				 cout<<arr[j]<<" ";
			 }
			 }

			 if(temp1-1==n)
			 temp1=0;
			 else
			 temp1++;
			 }
			 cout<<"\n process "<<proc[init-1].id<<" send message to "<<proc[temp1-1].id<<"\n";
			 arr[i]=proc[init-1].id;
			 for(int j=0;j<=i;j++){
				 cout<<arr[j]<<" ";
			 }
			 i++;
			 max=-1;
			 for(j=0;j<i;j++)
			 {
			 if(max<arr[j])
			 max=arr[j];
			 }
			 cout<<"\nprocess "<<max<<" select as coordinator";

			 for(i=0;i<n;i++)
			 {
			 if(proc[i].id==max)
			 {
			 strcpy(proc[i].state,"active");
			// cout<<"\n"<<i<<" "<<proc[i].id<<"deactivate\n";
			 }}
			 init=active;
			 			 temp2=init;
			 			 temp1=init+1;
			 while(temp2!=temp1)
			 			 {
			 			 if(strcmp(proc[temp1-1].state,"active")==0 )
			 			 {
			 			 cout<<"\n process "<<proc[init-1].id<<" send message coordinator:"<<max<<" to process"<<proc[temp1-1].id<<"\n";

			 			 init=temp1;


			 			 i++;

			 			 }

			 			 if(temp1-1==n)
			 			 temp1=0;
			 			 else
			 			 temp1++;
			 			 }


		}
		break;



}
}
return 0;
}
