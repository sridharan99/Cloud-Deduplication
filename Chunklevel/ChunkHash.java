import java.io.*;
import java.security.MessageDigest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.google.gson.Gson;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ChunkHash
{
	static CuckooHashMap<String,String> map;
	ChunkHash()
	{
		try
		{
			if(new File("dedupe.txt").exists())
			{
				decompress();
				Gson gson=new Gson();
				Reader reader = new FileReader("dedupe.json");
				map = gson.fromJson(reader, CuckooHashMap.class);
			}
			else
			{
				map=new CuckooHashMap<String,String>();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static String stripExtension (String str) 
	{
		if (str == null) return null;
		int pos = str.lastIndexOf(".");
		if (pos == -1) return str;
		return str.substring(0, pos);
    	}
	public static void getChecksum(String file,String metapath)
	{
		try
		{
			File metafile=new File(metapath);
			metafile.createNewFile();
			InputStream fis=new FileInputStream(file);
			byte[] b=new byte[4194304];			
			int numbytes;
			String hashvalue;
			FileWriter fw = new FileWriter(metapath, true);
			BufferedWriter bw = new BufferedWriter(fw);
			do
			{
				MessageDigest mesdigest=MessageDigest.getInstance("MD5");
				numbytes=fis.read(b);
		                if(numbytes>0)
					mesdigest.update(b,0,numbytes);
				hashvalue=getHash(mesdigest.digest());
				bw.write(hashvalue);
				String content=new String(b);
				if(map.get(hashvalue)==null)
				{
					map.put(hashvalue,content);
				}
			}while(numbytes!=-1);
			fis.close();
			bw.close();
			fw.close();	
		}
		catch(Exception e)
		{
			System.out.println("Exception :"+e);
		}
	}

	public static String getHash(byte[] b)
	{
		String result = "";
       		for (int i=0; i < b.length; i++) 
	  	{
           		result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16);
      		}
       		return result;
		
	}
     	public void removeDedupe(String path)throws Exception
	{
		File[] files= new File(path).listFiles();
		for(File file :files)
		{
			if(file.isFile())
			{
				String metapath=file.getParent()+"/"+stripExtension(file.getName())+".src";
				getChecksum(file.getAbsolutePath(),metapath);
				file.delete();
			}
			else
			{
				if(!((file.getName()).equals("..") || (file.getName()).equals(".")))
				removeDedupe( file.getAbsolutePath()+"/");
			}
		}
	}
	public static void compress(String data) throws IOException 
	{
		File f=new File("dedupe.json");
		f.delete();
		ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length());
		GZIPOutputStream gzip = new GZIPOutputStream(bos);
		gzip.write(data.getBytes());
		gzip.close();
		OutputStream os= new FileOutputStream("dedupe.txt");
		bos.writeTo(os);
		bos.close();
		os.close();
		
	}
	public static void decompress() throws IOException
	{
		FileInputStream fis = new FileInputStream("dedupe.txt");
            	GZIPInputStream gis = new GZIPInputStream(fis);
            	FileOutputStream fos = new FileOutputStream("dedupe.json");
            	byte[] buffer = new byte[4194304];
            	int len;
            	while((len = gis.read(buffer)) != -1)
		{
                	fos.write(buffer, 0, len);
            	}
		fos.close();
		gis.close();
		File f=new File("dedupe.txt");
		f.delete();
	}
	public static void main(String args[]) throws Exception
	{
		ChunkHash f=new ChunkHash();
		f.removeDedupe("Test/");
		Gson gson=new Gson();
		compress(gson.toJson(map));			
	}
}
