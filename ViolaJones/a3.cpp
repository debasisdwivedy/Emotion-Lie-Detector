// B657 assignment 3 skeleton code, D. Crandall
//
// Compile with: "make"
//
// This skeleton code implements nearest-neighbor classification
// using just matching on raw pixel values, but on subsampled "tiny images" of
// e.g. 20x20 pixels.
//
// It defines an abstract Classifier class, so that all you have to do
// :) to write a new algorithm is to derive a new class from
// Classifier containing your algorithm-specific code
// (i.e. load_model(), train(), and classify() methods) -- see
// NearestNeighbor.h for a prototype.  So in theory, you really
// shouldn't have to modify the code below or the code in Classifier.h
// at all, besides adding an #include and updating the "if" statement
// that checks "algo" below.
//
// See assignment handout for command line and project specifications.
//
#include "CImg.h"
#include <ctime>
#include <iostream>
#include <iomanip>
#include <stdlib.h>
#include <string>
#include <vector>
#include <sys/types.h>
#include <dirent.h>
#include <map>
#include <numeric>
#include <fstream>
#include <cmath>
#include <algorithm>
//Use the cimg namespace to access the functions easily
using namespace cimg_library;
using namespace std;

// Dataset data structure, set up so that e.g. dataset["bagel"][3] is
// filename of 4th bagel image in the dataset
typedef map<string, vector<string> > Dataset;

#include <ViolaJones.h>

// Figure out a list of files in a given directory.
//
vector<string> files_in_directory(const string &directory, bool prepend_directory = false)
{
  vector<string> file_list;
  DIR *dir = opendir(directory.c_str());
  if(!dir)
    throw std::string("Can't find directory " + directory);
  
  struct dirent *dirent;
  while ((dirent = readdir(dir))) 
    if(dirent->d_name[0] != '.')
      file_list.push_back((prepend_directory?(directory+"/"):"")+dirent->d_name);

  closedir(dir);
  return file_list;
}
//DEBUGGING
	CImg<double> integralImg(const CImg<double> &img)
	{
   		CImg<double> a(img.width(),img.height());
		a(0,0)=img(0,0);
		double temp1;
		double temp2;
		double temp3;
		double temp4;
		for(int i=0; i<img.width();i++)
		{
			for(int j=0; j<img.height(); j++)
			{
				temp1=img(i,j);
				if((j-1)>=0)
				temp2=a(i,j-1);
				else
				temp2=0.0;
      
				if((i-1)>=0)
				temp3=a(i-1,j);
				else
				temp3=0.0;
     	 
				if(((i-1)>=0)&&((j-1)>=0))
				temp4=a(i-1,j-1);
				else
				temp4=0.0;
      
				a(i,j)=temp1+temp2+temp3-temp4;
			}
		}
		return a;
	}

	vector<double> initialize_weights()
	{
		return vector<double>(60,0.0166667);
	}

	double sum_vector(vector<double> &a)
	{
		double sum=0;
		for(int i=0; i<a.size(); i++)
		{
			sum+=a[i];
		}
		return sum;
	}
	void normalize_weights(vector<double> &weights)
	{
		double sum=sum_vector(weights);
		cout<<sum<<endl;
		for(vector<double>::iterator iter=weights.begin();iter!=weights.end();iter++)
		{
			*iter=*iter/sum;
		}
	}


//DEBUGGING
int main(int argc, char **argv)
{
  try {
    if(argc < 2)
      throw string("Insufficent number of arguments");

    string mode = argv[1];
    

    // Scan through the "train" or "test" directory (depending on the
    //  mode) and builds a data structure of the image filenames for each class.
    Dataset filenames; 
    vector<string> class_list = files_in_directory(mode);
    for(vector<string>::const_iterator c = class_list.begin(); c != class_list.end(); ++c)
      filenames[*c] = files_in_directory(mode + "/" + *c, true);


	vector<vector<int> > features=ViolaJones::featureGenerator(24,24);
	cout<<features[107424][0]<<"  "<<features[107424][1]<<"  "<<features[107424][2]<<"  "<<features[107424][3]<<"  "<<features[107424][4]<<"  "<<endl;




//	ViolaJones *vj=new ViolaJones(filenames);
//	vj->AdaBoost(100);
	
	
//	vj->checkdata("data.txt");
//	delete vj;
/*
	vector<double> weights=initialize_weights();
	for(int i=0;i<5;i++)
	{
		weights[i]=0.00333;
	}
	normalize_weights(weights);
	for(int i=0;i<weights.size();i++)
	{
		cout<<weights[i]<<endl;
	}

	cout<<sum_vector(weights);
	



	CImg<double> img(mode.c_str());
	CImg<double> resizedGray=img.resize(24,24,1,3).get_RGBtoYCbCr().get_channel(0);
	CImg<double> integ=integralImg(resizedGray);
	resizedGray.save("gray.png");
	integ.save("integral.png");
	ofstream ofs("gray.txt");
	ofstream ofs1("integral.txt");
	for(int i=0;i<24;i++)
	{
		for(int j=0;j<24;j++)
		{
			ofs<<resizedGray(j,i)<<"  ";
			ofs1<<integ(j,i)<<"  ";
		}
		ofs<<endl;
		ofs1<<endl;
	}
*/		
    
  }
  catch(const string &err) {
    cerr << "Error: " << err << endl;
  }
}







