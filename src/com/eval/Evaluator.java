package com.eval;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Evaluator {
	
	static HashMap<String,String> queries=new HashMap<String, String>();
	static HashMap<String,Double> avgPrecisions=new HashMap<String,Double>();
	static HashMap<String,Double> pAt20=new HashMap<String,Double>();
	
	static List<String> relDocs =new ArrayList<String>();
	static List<Double> lstIDCG=new ArrayList<Double>();
	static List<List<String>> evalMetrics=new ArrayList<List<String>>();
	static List<Double> precisionLst=new ArrayList<Double>();
	
	public static void main(String args[]) throws Exception
	{
		String queryCode=null;
		String resultFile=null;
		System.out.println("Enter the relevance file::");
		String relFile=
				new BufferedReader(new InputStreamReader(System.in)).readLine();
		while(!"n".equalsIgnoreCase(queryCode) || !"n".equalsIgnoreCase(resultFile))
		{
		System.out.println("Enter the query code (press n to continue)::");;
		queryCode=
				new BufferedReader(new InputStreamReader(System.in)).readLine();
		if ("n".equalsIgnoreCase(queryCode))
			break;
		System.out.println("Enter the result file (press n to continue)::");
		resultFile=
				new BufferedReader(new InputStreamReader(System.in)).readLine();
		if ("n".equalsIgnoreCase(resultFile))
			break;
		queries.put(queryCode, resultFile);
		}
		System.out.println("Enter the evaluation file path:");
		String evalPath=
				new BufferedReader(new InputStreamReader(System.in)).readLine();
		
		for(Entry<String,String> query: queries.entrySet())
		{
			updateRelevance(relFile, query.getKey());
			calcIDCG();
			sysoIDCG();
			calcEvalMetrics(query.getValue(),query.getKey());
			writeToFile(evalPath+"/q"+query.getKey()+"_"+new SimpleDateFormat("MM-dd-yy_HH.mm.ss").format(new Date())+".out");
		}
		//System.out.println("avg prec::"+avgPrecisions.get(0));
		if(avgPrecisions.size()>0 && pAt20.size()>0)
			writePrecisionToFile(evalPath+"/precision_"+new SimpleDateFormat("MM-dd-yy_HH.mm.ss").format(new Date())+".out");
		System.out.println("End of evaluation::");
	}
	
	private static void sysoIDCG()
	{
		for(Double idcg:lstIDCG)
		{
			System.out.println("idcg::"+idcg);
		}
	}
	
	public static void writePrecisionToFile(String file) throws Exception
	{
		StringBuffer buff=new StringBuffer();
		Double totalAvgPre=0.0;
		
		buff.append("P@20");
		buff.append(System.getProperty("line.separator"));
		for(Entry<String,Double> p20:pAt20.entrySet())
		{
			buff.append(p20.getKey()+"::"+p20.getValue());
			buff.append(System.getProperty("line.separator"));
			System.out.println("P@20::"+p20.getKey()+"::"+p20.getValue());
		}
		buff.append(System.getProperty("line.separator"));
		
		buff.append("Average Precision");
		buff.append(System.getProperty("line.separator"));
		for(Entry<String,Double> avgPre: avgPrecisions.entrySet())
		{
			totalAvgPre=totalAvgPre+avgPre.getValue();
			buff.append(avgPre.getKey()+"::"+avgPre.getValue());
			buff.append(System.getProperty("line.separator"));
		}
		buff.append(System.getProperty("line.separator"));
		
		buff.append("Mean average precision(MAP)::"+(totalAvgPre/avgPrecisions.size()));
		System.out.println("Mean average precision::"+(totalAvgPre/avgPrecisions.size()));
		
		toFile(file, buff.toString());
		
	}
	
	public static void writeToFile(String file) throws Exception
	{
		
		
		StringBuffer buff=new StringBuffer();
		for(List<String> evalVals:evalMetrics)
		{
			for(String evalVal:evalVals)
			{
				buff.append(evalVal);
				buff.append(" ");
			}
			
			buff.append(System.getProperty("line.separator"));
		}
		
		System.out.println("buff::"+buff.toString());
		toFile(file, buff.toString());
	}
	
	private static void toFile(String file, String data) throws Exception
	{
		FileWriter fw=null;
		try
		{
			fw=new FileWriter(new File(file));
			fw.write(data);
		}
		finally
		{
			if(null!=fw)
				fw.close();
		}
		
	}
	
	public static void updateRelevance(String relFile, String queryCode) throws Exception
	{
		System.out.println("reading file::"+relFile+"::"+queryCode);
		FileInputStream fs=null;
		BufferedReader br=null;
		String line=null;
		String[] relData=null;
		relDocs=new ArrayList<String>();
		
		try
		{
			fs=new FileInputStream(new File(relFile));
			br=new BufferedReader(new InputStreamReader(fs));
			
			while(null!=(line=br.readLine()))
			{
				relData=line.split(" ");
				if(relData[0].equalsIgnoreCase(queryCode))
				{
					System.out.println("rel::"+relData[2].substring(relData[2].indexOf("-")+1));
					relDocs.add(relData[2].substring(relData[2].indexOf("-")+1));
				}
			}
		}
		finally
		{
			if(null!=br)
				br.close();
			if(null!=fs)
				fs.close();
		}
		
	}
	
	public static void calcIDCG()
	{
		lstIDCG=new ArrayList<Double>();
		if(relDocs.size()>0)
		{
			lstIDCG.add(1.0);
			for(int i=1; i<relDocs.size();i++)
			{
				lstIDCG.add(lstIDCG.get(i-1)+(1/(Math.log(i+1)/Math.log(2))));
			}
		}
			
	}
	
	public static void calcEvalMetrics(String resultFile,String queryCode) throws Exception
	{
		FileInputStream fs=null;
		BufferedReader br=null;
		
		String line=null;
		String[] resultData=null;
		double precision=0.0;
		double currRelDocs=0.0;
		double dcg=0.0;
		double prevDcg=0.0;
		double ndcg=0.0;
		double prevNdcg=0.0;
		double totalRelPrecision=0.0;
		
		List<String> evalVals=null;
		
		evalMetrics=new ArrayList<List<String>>();
		precisionLst=new ArrayList<Double>();
		
		int totalRelDocs=relDocs.size();
		
		try
		{
			fs=new FileInputStream(new File(resultFile));
			br=new BufferedReader(new InputStreamReader(fs));
			
			while(null!=(line=br.readLine()))
			{
				dcg=0.0;
				System.out.println("line::"+line);
				resultData=line.split(" ");
				
				evalVals=new ArrayList<String>();
				evalVals.add(resultData[3]);
				evalVals.add(resultData[2]);
				evalVals.add(resultData[4]);
				
				if(relDocs.contains(resultData[2]))
				{
					evalVals.add("1");
					currRelDocs++;
					if(evalMetrics.size()==0)
						dcg=1.0;
					else
						dcg= prevDcg+(1/(Math.log(evalMetrics.size()+1)/Math.log(2)));
					
					precision=currRelDocs/(precisionLst.size()+1);
					totalRelPrecision=totalRelPrecision+precision;
				}
				else
				{
					evalVals.add("0");
					dcg=prevDcg;
					precision=currRelDocs/(precisionLst.size()+1);
				}
				
				
				precisionLst.add(precision);
				evalVals.add(precision+"");
				
				evalVals.add((currRelDocs/totalRelDocs)+"");
				
				if(evalMetrics.size()<lstIDCG.size())
				{
					ndcg=(dcg/lstIDCG.get(evalMetrics.size()));
				}
				else
				{
					ndcg=(dcg/lstIDCG.get(lstIDCG.size()-1));
				}
				evalVals.add(ndcg+"");
				
				System.out.println("line vals::"+precision+"::"+dcg+"::"+ndcg+"::"+(currRelDocs/totalRelDocs));
				
				evalMetrics.add(evalVals);
				
				prevDcg=dcg;
				prevNdcg=ndcg;
			}
			
			pAt20.put(queryCode,Double.parseDouble(evalMetrics.get(19).get(4)));
			avgPrecisions.put(queryCode,totalRelPrecision/totalRelDocs);
			
		}
		finally
		{
			if(null!=br)
				br.close();
			if(null!=fs)
				fs.close();
		}
		
	}
}
