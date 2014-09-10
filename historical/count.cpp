#include <iostream>
#include <fstream>
#include <string>
#include <sstream>
#include <map>

struct count{
	int added;
	int modified;
	int removed;
};


using namespace std;
int main(int argc, char ** argv){
	if(argc!=2){
		cout << "Wrong number of arguments\n";
		return 1;
	}

	map<string,count> counter;

	ifstream ifs(argv[1]);
	if(!ifs){
		cout << "Fail to open file\n";
		return 2;
	}

	char c;
	string member;
	string line;
	while(getline(ifs,line)){
		stringstream ss;
		ss << line;
		ss >> c >> member;
		map<string,count>::iterator it;
		it = counter.find(member);
		struct count thiscount;
		if(it == counter.end()){
			thiscount.added = 0;
			thiscount.modified=0;
			thiscount.removed=0;
		}else{
			thiscount=it->second;	
		}
		if(c == '+')
			thiscount.added++;
		else if (c == '-')
			thiscount.removed++;
		else
			thiscount.modified++;
		counter[member]=thiscount;
	}

	ifs.close();

	map<string,count>::iterator it;
	for(it = counter.begin();it!=counter.end();it++){
		cout << it->first /*<< ": (" << it->second.added << ", " << it->second.modified << ", " << it->second.removed << ")\n"*/ << endl;
	}

	return 0;
}
