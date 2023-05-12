package ibf2022.batch2.csf.backend.repositories;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;


@Repository
public class ImageRepository {

	@Autowired
	private AmazonS3 s3Client;

	@Value("${do.storage.bucketname}")
	private String bucketName;

	// TODO: Task 3
	// You are free to change the parameter and the return type
	// Do not change the method's name
	public boolean upload(ZipEntry file, InputStream is, String name, String title, String comment) throws IOException {
		String dateUploaded = LocalDateTime.now().toString();
		// set UserData
		Map<String, String> userData = new HashMap<>();
		userData.put("name", name);
		userData.put("uploadDateTime", dateUploaded);
		userData.put("originalFilename", file.getName());
		userData.put("title", title);
		userData.put("comment", comment);

		// Retrieve fileName and construct contentType
		String fileName = file.getName();
		String[] fileList = fileName.trim().split(Pattern.quote("."));
		String contentType = "image/" + fileList[1];
		System.out.println(contentType);

		// set metaData of file
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(contentType);
		metadata.setContentLength(file.getSize());
		metadata.setUserMetadata(userData);

		PutObjectRequest putRequest = new PutObjectRequest(
				bucketName, fileName, is, metadata);
				putRequest.withCannedAcl(CannedAccessControlList.PublicRead);
		s3Client.putObject(putRequest);
		return true;


	}

	public String getDataURL(String key) throws IOException {
		GetObjectRequest getReq =  new GetObjectRequest(bucketName, key);
            S3Object result = s3Client.getObject(getReq);

            //! get object metadata
            // ObjectMetadata metaData = result.getObjectMetadata();
            // Map<String,String> userData = metaData.getUserMetadata();

			String encodedString = null;
            
            //! get object content as byte[]
            try(InputStream s3is = result.getObjectContent().getDelegateStream()){
                byte[] buffer = s3is.readAllBytes();
                encodedString = Base64.getEncoder().encodeToString(buffer); 
			}

			return encodedString;

}
}
