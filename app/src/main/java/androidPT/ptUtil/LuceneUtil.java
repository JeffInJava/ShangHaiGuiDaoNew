package androidPT.ptUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import android.text.TextUtils;


public class LuceneUtil {

	public static final String PAGE_CONTENT_="pageContent";
	public static final String PAGE_SIZE="pageSize";
	public static final String PDF_NAME="pdfName";
	public static final String TITLE="title";
	public static final String DEPARTMENTNOS="departmentNos";



	/**
	 * 创建索引
	 * @param indexDir 存放索引的文件夹
	 * @throws IOException
	 */
	//	public static List<Integer> createPdfIndex(List<LuceneBean> lbList,String indexDir) throws IOException{
	//		IndexWriter indexWriter = null;
	//		List<Integer> pageCountList=new ArrayList<Integer>();
	//
	//		try {
	//			//创建一个分析器，这里使用的是标准分析器，适用于大多数场景，并且在StandardAnalyzer中包括了部分中文分析处理功能
	//			//文件形式索引
	//			Directory directory = new SimpleFSDirectory(new File(indexDir));  
	//
	//			indexWriter = new IndexWriter(directory,new StandardAnalyzer(Version.LUCENE_30),true,IndexWriter.MaxFieldLength.UNLIMITED);
	//
	//			PdfReader pdfReader=null;
	//			InputStream in=null;
	//			//遍历文件
	//			for (LuceneBean lb:lbList) {
	//				File file=lb.getFile();
	//				if(file.isFile()){
	//					try {
	//						in = new FileInputStream(file);  
	//						pdfReader = new PdfReader(in);
	//
	//						Document document = new Document();
	//						// 下标从1开始
	//						for (int j = 1; j <=pdfReader.getNumberOfPages(); j++)
	//						{
	//							document.add(new Field(PAGE_CONTENT_+j,PdfTextExtractor.getTextFromPage(pdfReader,j),Field.Store.YES,Field.Index.ANALYZED));  
	//						}
	//						document.add(new Field(PAGE_SIZE, Integer.toString(pdfReader.getNumberOfPages()),Field.Store.YES,Field.Index.ANALYZED)); 
	//						document.add(new Field(PDF_NAME, file.getName(),Field.Store.YES,Field.Index.ANALYZED));  
	//						document.add(new Field(TITLE,lb.getTitle(),Field.Store.YES,Field.Index.ANALYZED));  
	//						String dtps=ArrayUtils.toString(lb.getDepartmentNos());
	//						dtps=dtps.substring(1,dtps.length()-1);
	//						document.add(new Field(DEPARTMENTNOS,dtps,Field.Store.YES,Field.Index.ANALYZED));
	//						indexWriter.addDocument(document);
	//
	//						pageCountList.add(pdfReader.getNumberOfPages());
	//
	//						MyLogUtil.printlnDebug("文件[" + file.getCanonicalPath()+ "]正在建立索引");
	//					}  finally{
	//						if(null!=pdfReader)pdfReader.close();
	//						if(null!=in)in.close();
	//					}
	//
	//				}
	//			}
	//			MyLogUtil.printlnDebug("被索引的文档个数:" + indexWriter.numDocs());
	//		} finally{
	//			if(indexWriter!=null){
	//				indexWriter.close();// 关闭writer
	//			}
	//		}
	//		return pageCountList;
	//	}


	/**
	 * 按页查询相关内容
	 * @param indexDir 存放索引的文件夹
	 * @param queryString 查询的字
	 * @param departmentNo 部门号
	 * @param searchSize 查询返回的数量
	 * @throws IOException 
	 * @throws ParseException
	 */
	public static List<Map<String,Object>> searchPageContentIndex(String indexDir,String queryString,String departmentNo,int searchSize) throws IOException, ParseException{
		//新建 IndexSearcher搜索工具
		IndexReader reader  = null;

		List<Map<String,Object>> mapList=new ArrayList<Map<String,Object>>();
		try {
			Directory dir = new SimpleFSDirectory(new File(indexDir));
			IndexSearcher indexSearch = new IndexSearcher(dir);
			reader=indexSearch.getIndexReader();
			Document doc =null;
			int pageSize=0;
			QueryParser queryParser = null;
			Query query =null;
			TopDocs results = null;
			ScoreDoc[] scoreDocs=null;
			Map<String,Object> map=null;
			List<String> pageContentList=null;
			List<Integer> pageList=null;

			List<List<String>> pageContents=null;
			List<List<Integer>> pages=null;
			String departmentNos=null;

			for(int i=0;i<reader.maxDoc();i++){
				map=new HashMap<String,Object>();
				doc=indexSearch.doc(i);
				pageSize=Integer.valueOf(doc.get(PAGE_SIZE));
				departmentNos=doc.get(DEPARTMENTNOS);
				//判断部门号
				if(!TextUtils.isEmpty(departmentNos) && !TextUtils.isEmpty(departmentNo)&&!departmentNo.equals("null")){
					String[] deptArray=departmentNos.split(",");
					boolean isCanReadDept=false;
					for(String dept:deptArray){
						if(departmentNo.equals(dept.trim())){
							isCanReadDept=true;
							break;
						}
					}
					if(!isCanReadDept){
						continue;
					}
				}

				pageContents=new ArrayList<List<String>>();
				pages=new ArrayList<List<Integer>>();

				for(int j=1;j<=pageSize;j++){
					queryParser=new QueryParser(Version.LUCENE_30,PAGE_CONTENT_+j, new StandardAnalyzer(Version.LUCENE_30));  
					query = queryParser.parse(queryString);  
					results = indexSearch.search(query, 10); 

					if(results.scoreDocs.length>0){
						map.put(PDF_NAME, doc.get(PDF_NAME));
						map.put(TITLE, doc.get(TITLE));

						scoreDocs =  results.scoreDocs;
						pageContentList=new ArrayList<String>();
						pageList=new ArrayList<Integer>();
						for (ScoreDoc scoreDoc : scoreDocs) {
							if(pageList.size()<10){
								pageList.add(j);
								pageContentList.add(indexSearch.doc(scoreDoc.doc).get(PAGE_CONTENT_+j));
							}
						}

						if(pages.size()<searchSize){
							pageContents.add(pageContentList);
							pages.add(pageList);
						}

					}


				}
				if(null!=pages && pages.size()>0){
					map.put("pages", pages);
					map.put("pageContents", pageContents);
					mapList.add(map);
				}
			}

		}finally{
			if(null!=reader){
				reader.close();
			}
		}

		return mapList;
	}

	public static void main(String[] args) throws IOException, ParseException{
		String indexDir="D:\\lucene\\index";
		//		createPdfIndex(new File("C:\\Users\\deep\\Desktop\\upload\\upload\\pdf"),indexDir,"tt");
		//		System.out.println(JtJSONArray.toJSONString(searchPageContentIndex(indexDir,"搜狗",10)));
	}

}
