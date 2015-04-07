package rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Cerveja;

import com.google.gson.Gson;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import dao.CervejaDao;

@Path("/cervisiamController")
public class cervisiamController 
{
	private final static String IMAGE_REPOSITORY = "C:/Developing/cervisiam/";
	
	@GET
	@Produces("text/json")
	@Path("/get/{username}")
	public String testService(@PathParam("username") String userName) 
	{
		return "Stub01 GET Message for " + userName;
	}
	
	@GET
	@Path("/image/{image}")
	@Produces("image/*")
    public Response getImage(@PathParam("image") String image) 
	{
		File file = new File(IMAGE_REPOSITORY + image);

		if (!file.exists()) {
			throw new WebApplicationException(404);
		}

		String mt = new MimetypesFileTypeMap().getContentType(file);
		return Response.ok(file, mt).build();
    }
	
	@GET
	@Produces("text/json")
	@Path("/get/cerveja")
	public String getAllCerveja() 
	{
		CervejaDao dao = new CervejaDao();

		List<Cerveja> cervejas = dao.getCerveja();

		String json = new Gson().toJson(cervejas);

		System.out.println(json);

		return json;		
	}
	
	@GET
	@Path("/delete/{id}")
	@Produces("text/plain")
	public String deleteCervejaById(@PathParam("id") Long id)
	{
		CervejaDao dao = new CervejaDao();
		Cerveja cerveja = new Cerveja();
		cerveja.setId(id);
		if ( dao.delete(cerveja) )
			return "Cerveja deletada com sucesso!";
		
		return "Falha ao deletar cerveja";
	}

	@POST
	@Path("/save")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String uploadFile(@FormDataParam("photo") InputStream uploadedInputStream,
							 @FormDataParam("photo") FormDataContentDisposition fileDetail, 
							 @FormDataParam("name") String name,
							 @FormDataParam("brand") String brand,
							 @FormDataParam("avaliation") Integer star,
							 @FormDataParam("comment") String comment) 
	{	
		System.out.println(name);
		System.out.println(brand);
		System.out.println(comment);
		System.out.println(star);
		System.out.println(fileDetail);
		
		Cerveja cerveja = new Cerveja();
		cerveja.setName(name);
		cerveja.setBrand(brand);
		cerveja.setComment(comment);
		cerveja.setStar(star);
		
		CervejaDao dao = new CervejaDao();
		
		if ( !dao.save(cerveja) )
			return "Falha ao salvar cerveja!";
		
		if ( !"".equals(fileDetail.getFileName()) )
		{
			String uploadedFileLocation = IMAGE_REPOSITORY + fileDetail.getFileName();
			cerveja.setImage(fileDetail.getFileName());
			dao.saveOrUpdate(cerveja);
			writeToFile(uploadedInputStream, uploadedFileLocation);
		}

		return "Cerveja salva com sucesso!";
	}

	private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) 
	{
		try 
		{
			OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
