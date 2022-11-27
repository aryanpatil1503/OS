//============================================================================
// Name        : assignment2_LP_sem5.cpp
// Author      : 
// Version     :
// Copyright   : Your copyright notice
// Description : Hello World in C++, Ansi-style
//============================================================================

#include <iostream>
#include<bits/stdc++.h>
using namespace std;

class PageReplacement
{
	private:
		int f,n,hit,page_f_cnt,top;
		vector<int> p;		//reference string
		vector<int> frames;		//page frame

	public:
		void input();
		void fifo();

		void optimal();
		void lru();
		void display();
		void display_pgefault();
		void initialize();
		PageReplacement(){
			f=0;
			n=0;
			hit=0;
			page_f_cnt=0,
			top=0;
		};
		int is_hit(int);
};
void PageReplacement::initialize(){
	page_f_cnt=0;
	top=0;
	frames.resize(f);
	for(int i=0;i<f;i++){
		frames[i]=9999;
	}
}
void PageReplacement::input(){
	cout<<"\n Enter number of Frames : ";
	cin>>f;
	cout<<"\n Enter number of Pages : ";
	cin>>n;
	p.resize(n);
    cout<<"\n Enter Pages : ";
    for(int i=0; i<n; i++)
    {
        cin>>p[i];
    }

}

int PageReplacement::is_hit(int d){
	hit=0;
	for(int i=0;i<f;i++){
		if(frames[i]==d){
			hit=1;
			break;
		}
	}
	return hit;
}

void PageReplacement::display(){

	cout<<"\n Pages :";
	for(int i=0;i<f;i++){
		if(frames[i]!=9999){
			cout<<frames[i]<<" ";
		}
	}

}
void PageReplacement::display_pgefault(){
	cout<<"\n total number of page faults: "<<page_f_cnt;
}

void PageReplacement:: fifo(){
	initialize();
	for(int i=0;i<n;i++){

		cout<<"\n after adding page: "<<p[i]<<" pages are:";
		if(is_hit(p[i])==0){
			//int j=0;
			//int j;
			/*for(j=0;j<f-1;j++){
				frames[j]=frames[j+1];

			}*/
			frames[top]=p[i];
			top++;
			if(top>=f){
				top=0;
			}


			//frames[j]=p[i];
			page_f_cnt++;
			display();

	}
		else{
			cout<<"\n No page fault";
		}

		display_pgefault();
		cout<<endl;
}
//display();
};



void PageReplacement:: lru(){
	initialize();
	int least[n];			//for occurrences
	for(int i=0;i<n;i++){

		cout<<"\n after adding page: "<<p[i];
		if(is_hit(p[i])==0){

			int j=0;
			for(j=0;j<f;j++){

				int pg=frames[j];
				int found=0;
				int k=0;
				for(k=i-1;k>=0;k--){

					if(pg==p[k]){
						least[j]=k;		//if occurred before put recent index
						found=1;
						break;
					}
					else{
						found=0;
					}
				}
					if(!found){
						least[j]=-9999;		//if not occurred before
					}
			}
			int min=9999;
			int repindex;		//index for least recent
			for(int j=0;j<f;j++){		//to find least of those occurrence
				if(least[j]<min){
					min=least[j];
					repindex=j;
				}
			}
			frames[repindex]=p[i];		//add in place of least recent occurred
			page_f_cnt++;
			//display();
			display_pgefault();
		}
		else{
			cout<<"\n No page fault";
		}
		display();
	}

}

void PageReplacement::optimal(){
	initialize();
	int right[n]; 		//to check to the right of page not occurring for long time
	for(int i=0;i<n;i++){
		cout<<"\n after adding page: "<<p[i];
		if(is_hit(p[i])==0){

			int j;
			for(j=0;j<f;j++){

				int pg=frames[j];
				int found=0;
				int k=0;
				for(k=i;k<20;k++){

					if(pg==p[k]){
						right[j]=k;		//if occurred to the right of page put recent index
						found=1;
						break;
					}
					else{
						found=0;
					}
				}
					if(!found){
						right[j]=9999;		//if not occurred
					}
			}
			int max=-9999;
			int repindex;	//index for page not used for longest time

			for(j=0;j<3;j++){		//to find highest of those occurrence
				if(right[j]>max){

					max=right[j];
					repindex=j;
				}
			}
			frames[repindex]=p[i];
			page_f_cnt++;
			display();
			display_pgefault();
		}
		else{
			cout<<"\n No page fault";
		}

	}


}



int main() {

	PageReplacement obj;
	obj.input();
	int choice;
	while(1){
		cout<<"\nPage Replacement Algorithms\n1.FIFO\n2.Optimal\n3.LRU\n4.Exit\nEnter your choice:";
		cin>>choice;
		switch(choice){
		case 1:
			obj.fifo();
			break;
		case 2:
			obj.optimal();
			break;
		case 3:
			obj.lru();
			break;
		case 4:
			cout<<"\n Thank you";
			exit(0);
		}
	}

	return 0;
}
