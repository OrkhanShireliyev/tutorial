package com.example.springtutorialapp.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.example.springtutorialapp.model.Tutorial;
import com.example.springtutorialapp.repository.TutorialRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class S3Service {

    private TutorialRepositoryJpa tutorialRepositoryJpa;
    private final AmazonS3 amazonS3;
    private final String bucketName ="bucket-name";

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Autowired
    public void tutorialRepositoryJpa(TutorialRepositoryJpa tutorialRepositoryJpa){
        this.tutorialRepositoryJpa=tutorialRepositoryJpa;
    }

    public S3Service() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials("access-key", "secretKey");
        this.amazonS3 = AmazonS3ClientBuilder
                .standard()
                .withRegion("eu-north-1")
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        String keyName=UUID.randomUUID().toString()+"_"+multipartFile.getOriginalFilename();
        File file=convertMultiPartFileToFile(multipartFile);
        amazonS3.putObject(bucketName,keyName,file);
        file.delete();
        return amazonS3.getUrl(bucketName,keyName).toString();
    }

    public static File convertMultiPartFileToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public S3Object downloadFile(String keyName){
        return amazonS3.getObject(bucketName, keyName);
    }

    public ResponseEntity<byte[]> downloadImage(Long id) {
        System.out.println("downloadImage metoduna daxil oldu");
        Optional<Tutorial> tutorialOpt = tutorialRepositoryJpa.findById(id);
        System.out.println("downloadImage metodunun daxilində obyekti yoxladı");
        System.out.println(tutorialOpt);

        if (tutorialOpt.isPresent()) {
            System.out.println("downloadImage metodunun daxilində ifi daxil oldu");

            Tutorial tutorial = tutorialOpt.get();
            String keyName = "2db93939-c115-4f10-aba3-2253aa9484c4_91xUz2EuYdL._AC_UF1000,1000_QL80_.jpg";  // S3-də saxlanılan şəkilin adını əldə edin (URL deyil, key)

            S3Object s3Object = amazonS3.getObject(bucketName, keyName);  // S3-dən şəkili alın
            S3ObjectInputStream inputStream = s3Object.getObjectContent();

            try {
                byte[] imageBytes = IOUtils.toByteArray(inputStream);  // Şəkili byte array kimi oxuyun
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)  // Şəkilin formatını göstərin (JPEG, PNG, və s.)
                        .body(imageBytes);  // Şəkilin byte[] arrayini qaytarın
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
