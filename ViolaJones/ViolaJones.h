class ViolaJones {
private: 
	vector<vector<int> > features;
	static const int size=24;  // subsampled image resolution
	int NumOfImg;
	Dataset filenames;
	double **data;
	double *data_area;
	int **marker;
        int *marker_area;	

public:
	ViolaJones(const Dataset &filenames1)
	{
		this->filenames=filenames1;
		NumOfImg=ImgNum();
		features=featureGenerator(size, size);
		data=0;
		data_area=0;
		marker=0;
		marker_area=0;
		DataPrepare();

	} //Initialize the memebers with functions
  
	~ViolaJones()
	{
		deallocate_storage();
	}



//For testing

	struct test
	{
		int a;
		double b;
	};
	
	static bool CompareByA(const test &x, const test &y)
	{
		return x.a<x.b;
	}
	void testF()
	{
		vector<test> test_vector;
		test temp;
		for(int i=0; i<10; i++)
		{
			temp.a=10-i;
			temp.b=10.00*i;
			test_vector.push_back(temp);
		}
	
		sort(test_vector.begin(), test_vector.end(), CompareByA);
	}

	void checkfeatures(string filename)
	{
		ofstream ofs(filename.c_str());
		for(int i=0;i<features.size();i++)
		{
			for(int j=0;j<5;j++)
			{
				ofs<<features[i][j]<<"  ";
			}
			ofs<<endl;
		}
	}
	
	void checkdata(string filename)
	{
		ofstream ofs(filename.c_str());
		for(int i=0;i<NumOfImg;i++)
		{
			for(int j=6000;j<6050;j++)
			{
				ofs<<data[i][j]<<"  ";
			}
			ofs<<endl;
	
		}
	}
	
//Utilities

	int ImgNum()
	{
		int NumOfImg=0;
		for(Dataset::const_iterator c_iter=filenames.begin();c_iter!=filenames.end();++c_iter)
		{
			NumOfImg+=c_iter->second.size();
		}	
		return NumOfImg;
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
//
//I would like to break the AdaBoost function into different parts, the following is the broken parts
	
	struct weak_classifier
	{
		double thresh;
		double error_rate;	
		int feature_idx;
	};
	

//used to contain feature value and class, that is (xi, yi)
	struct data_n_marker
	{
		double data;
		int marker;
	};
//For vector<data_n_marker> sort
        static bool CompareByData(const data_n_marker &a, const data_n_marker &b)
	{
		return a.data<b.data;// guess this is ascending 
	}


	vector<double> initialize_weights()
	{
		return vector<double>(NumOfImg,1);
	}
	void normalize_weights(vector<double> &weights)
	{
		double sum=sum_vector(weights);
		for(vector<double>::iterator iter=weights.begin();iter!=weights.end();iter++)
		{
			*iter=*iter/sum;
		}
	}

	void update_weights(vector<double> &weights,double error,vector<double> &ei)
	{
		double beta_t=error/(1-error);
	//	cout<<"Beta_T: "<<beta_t<<endl;
	//	cout<<"New Weights"<<endl;
		for(int i=0;i<weights.size();i++)
		{
			weights[i]=weights[i]*pow(beta_t,1-ei[i]);
	//		cout<<weights[i]<<endl;
		}
	}

	
	
	weak_classifier weak(int feature_idx, const vector<double> &weights)
	{

//		cout<<"      Feature ID: "<<feature_idx<<endl;
		
		vector<data_n_marker> feature_vector;
		data_n_marker temp;
		//Copy data and corresponding marker to feature_vector
		for(int i=0;i<NumOfImg;i++)
		{
			temp.data=data[i][feature_idx];
			temp.marker=marker[i][feature_idx];
			feature_vector.push_back(temp);
		}
		

		std::sort(feature_vector.begin(),feature_vector.end(), CompareByData);
	
	//	for(int i=0; i< feature_vector.size(); i++)
	//	{
	//		cout<<feature_vector[i].data<<"  "<<feature_vector[i].marker<<endl;
	//	
	//	}
	
		//Initialize 
		double error_front=abs(-1-feature_vector[0].marker)*weights[0];
		double error_back=0;
		for(int p=1;p<NumOfImg;p++)
		{
			error_back+=abs(1-feature_vector[p].marker)*weights[p];
		}


		double error_total=error_front+error_back;
		double error_min=error_total;
		int flag=0;
		double local_error_front=0;
		double local_error_back=0;
		//Do toggle
		for(int j=1;j<NumOfImg;j++)
		{
			local_error_front=abs(-1-feature_vector[j].marker)*weights[j];
			local_error_back=abs(1-feature_vector[j].marker)*weights[j];
			error_total=error_total-local_error_back+local_error_front;
			if(error_total<error_min)
			{
				flag=j;
				error_min=error_total;
			}			
		}
		
		weak_classifier w_class;
		w_class.thresh=feature_vector[flag].data;
		w_class.error_rate=error_min/2.0;
		w_class.feature_idx=feature_idx;
		
//		cout<<"Break Point  "<<flag<<"   Error Rate   "<<w_class.error_rate<<endl;	
		//FOR DEBUGGING
//	        cout<<"Threshold "<<w_class.thresh<<endl;
//		cout<<"Error_rate "<<w_class.error_rate<<endl;
//		cout<<"Feature ID  "<<w_class.feature_idx<<endl;

		//DEBUGGINF DONE

		return w_class;
	}

	
	weak_classifier best_in_weak(const vector<double> &weights)
	{
		weak_classifier output_classifier=weak(0,weights);
		weak_classifier local_classifier;
		int count=0;
		for(int i=1; i< features.size(); i++)
		{	
			
			local_classifier=weak(i,weights);
			//DEBUGGING
		//	if(count<100)
		//		cout<<local_classifier.error_rate<<endl;
		//	count++;
			
			//DEBUGGING

			if(local_classifier.error_rate<output_classifier.error_rate)
			{
				output_classifier.thresh=local_classifier.thresh;
				output_classifier.error_rate=local_classifier.error_rate;
				output_classifier.feature_idx=i;
			}
		}


		return output_classifier;

	}		

	vector<double> get_ei(const weak_classifier &best_classifier)
	{
		vector<double> output_ei(NumOfImg,0);
		double prediction=0;
		for(int i=0; i<NumOfImg; i++)
		{
			//Get prediction from threshold
			if(data[i][best_classifier.feature_idx]<best_classifier.thresh)
				prediction=-1;
			else 
				prediction=1;
			//Compare the prediction and real marker
			if(marker[i][best_classifier.feature_idx]==prediction)
				output_ei[i]=0;
			else
				output_ei[i]=1;
		}

		return output_ei;
	}

	typedef vector<weak_classifier> strong_classifier;
	
	void Test()
	{
//		vector<double> weights=initialize_weights();
//		normalize_weights(weights);
//		weak_classifier temp;
//		for(int i=5200; i< 5500; i++)
//		{
//			temp=weak(i,weights);
//		}
		
//		for(int i=5000;i<5011;i++)
//		cout<<"Feature"<<i<<":  "<<features[i][0]<<"  "<<features[i][1]<<"  "<<features[i][2]<<"  "<<features[i][3]<<"  "<<features[i][4]<<endl;
		

	}


	strong_classifier AdaBoost(int rounds)
	{
		cout<<endl<<endl<<endl;
		cout<<"Adaptive Boosting"<<endl;
		cout<<"...................................................."<<endl;
	
		ofstream ofs("strong.txt");
	
		strong_classifier output_classifier;
		vector<double> ei_vector;
                vector<double> weights=initialize_weights();
		weak_classifier best_classifier;
		for(int i=0;i<rounds;i++)
		{
			cout<<"Round:  "<<i+1<<endl;

		

			normalize_weights(weights);
	
			ofs<<"Weights in Round "<<i+1<<endl;
			for(int j=0;j<weights.size();j++)
			{
				ofs<<weights[j]<<"  ";
			}			
			ofs<<endl;
		

			best_classifier=best_in_weak(weights);
			ei_vector=get_ei(best_classifier);
			update_weights(weights,best_classifier.error_rate,ei_vector);
			output_classifier.push_back(best_classifier);
			

		
			ofs<<"ei in Round "<<i+1<<endl;
			for(int k=0;k<ei_vector.size();k++)
			{
				ofs<<ei_vector[k]<<"  ";

			}
			ofs<<endl;


			cout<<"Error Rate: "<<best_classifier.error_rate<<endl;
			cout<<"Feature ID: "<<best_classifier.feature_idx<<endl<<endl;
//			ofs<<"Round "<<i+1<<":"<<endl;
//			ofs<<"Break Point: "<<best_classifier.feature_idx;
//			ofs<<"    Threshold: "<<best_classifier.thresh;
//			ofs<<"    Error Rate: "<<best_classifier.error_rate<<endl<<endl;
		}
		cout<<"...................................................."<<endl;
		cout<<"Adaptive Bppsting Finshed"<<endl;		
		return output_classifier;
	
	}	


//End parts of AdaBoost


	//Data set of the face and non face; 
	void DataPrepare()
	{
		cout<<"Data Preparation"<<endl;
		cout<<"................................................................."<<endl;
		initialize_storage();
		string CategoryName;
		int flag;
		int r=0;
		for(Dataset::const_iterator c_iter=filenames.begin();c_iter!=filenames.end();++c_iter)
		{	
			
			CategoryName=c_iter->first;

			if(CategoryName=="face")
				flag=1;
			else
				flag=-1;
			for(int i=0;i<c_iter->second.size();i++,r++)//Need to be modified iterater
			{
				cout<<100*double(r+1)/double(NumOfImg)<<"%"<<endl;
				cout<<c_iter->second[i]<<endl;
				CImg<double> temp=extract_features(c_iter->second[i].c_str());//resize the image
				for(int j=0;j<features.size();j++)
				{
					data[r][j]=doFilter(temp,features[j]);//fill the data		
					marker[r][j]=flag;
				}
			}
		}
		cout<<"................................................................."<<endl;
		cout<<"Data Preparation Done"<<endl;
	}


	void train(const Dataset &filenames) 
	{
    
	}
  
	string classify(const string &filename)
	{}
  
	void load_model()
	{}

//protected:

  // extract features from an image, which in this case just involves resampling and 
  // rearranging into a vector of pixel data.
	CImg<double> extract_features(const string &filename)
	{
		CImg<double> img(filename.c_str());
		CImg<double> resizedGray=img.resize(size,size,1,3).get_RGBtoYCbCr().get_channel(0);
		
		return resizedGray;
	}
  
	
  	//This function is copied and modified from Assignment1 DTwoDimArray.h  	

	void deallocate_storage()
   	{
     		if(data)
		{
			delete[] data;
	  		delete[] data_area;
	  
	 		data = 0;
	 		data_area = 0;
		}
    		if(marker)
		{
			delete[] marker;
	  		delete[] marker_area;
	  
	 		marker = 0;
	 		marker_area = 0;
		}
    	}

	void initialize_storage()
	{
		int _rows=NumOfImg;
		int _cols=features.size();
		
		cout<<_rows<<"   "<<_cols<<endl;
	
		if(data)
      			deallocate_storage();
    
   		if(_rows > 0)
     		{
			data = new double *[_rows];
			marker=new int *[_rows];
			data_area = new double[_rows * _cols];
			marker_area=new int[_rows*_cols];
     		}
  		else
     		{
			data = 0;
			marker=0;
			data_area = 0;
			marker_area=0;
     		}
    
		double  *cp = data_area;
		int  *cp1=marker_area;
   		for(int i=0; i<_rows; i++, cp+=_cols,cp1+=_cols)
     		{
			data[i] = cp;
			marker[i]=cp1;
		}
		cout<<"done"<<endl;
	}

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

	double AreaCalculator(const CImg<double> &InteImg, int x1, int y1, int x2, int y2)
	{
		double output;
		double temp1;
		double temp2;
		double temp3;
		double temp4;

		if((x1-1)>=0&&(y1-1)>=0)
			temp1=InteImg(x1-1,y1-1);
		else
			temp1=0.0;
		
		temp2=InteImg(x2,y2);
		if((x1-1)>=0)
			temp3=InteImg(x1-1,y2);	
		else 
			temp3=0.0;
		
		if((y1-1)>=0)
			temp4=InteImg(x2,y1-1);
		else
			temp4=0.0;

//		output=InteImg(x1,y1)+InteImg(x2,y2)-(InteImg(x1,y2)+InteImg(x2,y1));
  		output=temp1+temp2-temp3-temp4;
		return output;
	}

	static vector<vector<int> > featureGenerator(int xdim, int ydim)
	{
		vector<vector<int> > output;//1: type, 2: x. 3: y, 4: width, 5: height
		vector<int> temp(5,0);
		
		for(int i=0;i<xdim-1;i++)
		{
			for(int j=0;j<ydim;j++)
			{
				for(int p=1;p<(xdim-i)/2+1;p++)
				{
					for(int q=1;q<(ydim-j)+1;q++)
					{
						temp[0]=1;
						temp[1]=i;
						temp[2]=j;
						temp[3]=p;
						temp[4]=q;
						output.push_back(temp);
					}
				}
			}
		}

		for(int i=0;i<xdim;i++)
		{
			for(int j=0;j<ydim-1;j++)
			{
				for(int p=1;p<(xdim-i)+1;p++)
				{
					for(int q=1;q<(ydim-j)/2+1;q++)
					{
						temp[0]=2;
						temp[1]=i;
						temp[2]=j;
						temp[3]=p;
						temp[4]=q;
						output.push_back(temp);
					}
				}
			}
		}

		for(int i=0;i<xdim-2;i++)
		{
			for(int j=0;j<ydim;j++)
			{
				for(int p=1;p<(xdim-i)/3+1;p++)
				{
					for(int q=1;q<(ydim-j)+1;q++)
					{
						temp[0]=3;
						temp[1]=i;
						temp[2]=j;
						temp[3]=p;
						temp[4]=q;
						output.push_back(temp);
					}
				}
			}
		}

		for(int i=0;i<xdim;i++)
		{
			for(int j=0;j<ydim-2;j++)
			{
				for(int p=1;p<(xdim-i)+1;p++)
				{
					for(int q=1;q<(ydim-j)/3+1;q++)
					{
						temp[0]=4;
						temp[1]=i;
						temp[2]=j;
						temp[3]=p;
						temp[4]=q;
						output.push_back(temp);
					}
				}
			}
		}

		for(int i=0;i<xdim-1;i++)
		{
			for(int j=0;j<ydim-1;j++)
			{
				for(int p=1;p<(xdim-i)/2+1;p++)
				{
					for(int q=1;q<(ydim-j)/2+1;q++)
					{
						temp[0]=5;
						temp[1]=i;
						temp[2]=j;
						temp[3]=p;
						temp[4]=q;
						output.push_back(temp);
					}
				}
			}
		}


	return output;
	}

	//img is the gray scale image or one channel of image
	//a is feature coordinates
	double doFilter(const CImg<double> &img, vector<int> &a)
	{
		double output;
		CImg<double> InteImg=integralImg(img);
		switch(a[0])
		{
		case 1:
		output=AreaCalculator(InteImg,a[1],a[2],a[1]+a[3]-1,a[2]+a[4]-1);
		output=output-AreaCalculator(InteImg,a[1]+a[3],a[2],a[1]+a[3]*2-1,a[2]+a[4]-1);
		break;
		case 2:
		output=AreaCalculator(InteImg,a[1],a[2],a[1]+a[3]-1,a[2]+a[4]-1);
		output=output-AreaCalculator(InteImg,a[1],a[2]+a[4],a[1]+a[3]-1,a[2]+a[4]*2-1);
		break;
		case 3:
		output=AreaCalculator(InteImg,a[1],a[2],a[1]+a[3]-1,a[2]+a[4]-1);
		output=output-AreaCalculator(InteImg,a[1]+a[3], a[2], a[1]+a[3]*2-1, a[2]+a[4]-1);
		output=output+AreaCalculator(InteImg,a[1]+a[3]*2, a[2], a[1]+a[3]*3-1, a[2]+a[4]-1);
		break;
		case 4:
		output=AreaCalculator(InteImg,a[1], a[2], a[1]+a[3]-1, a[2]+a[4]-1);
		output=output-AreaCalculator(InteImg,a[1], a[2]+a[4], a[1]+a[3]-1, a[2]+a[4]*2-1);
		output=output+AreaCalculator(InteImg,a[1], a[2]+a[4]*2, a[1]+a[3]-1, a[2]+a[4]*3-1);
		break;
		case 5:
		output=AreaCalculator(InteImg,a[1], a[2], a[1]+a[3]-1, a[2]+a[4]-1);
		output=output-AreaCalculator(InteImg,a[1]+a[3], a[2], a[1]+a[3]*2-1, a[2]+a[4]-1);
		output=output-AreaCalculator(InteImg,a[1], a[2]+a[4], a[1]+a[3]-1, a[2]+a[4]*2-1);
		output=output+AreaCalculator(InteImg,a[1]+a[3], a[2]+a[4], a[1]+a[3]*2-1, a[2]+a[4]*2-1);
		break;
		default: output=0.0;
		}
  
	return output;
	}

};
