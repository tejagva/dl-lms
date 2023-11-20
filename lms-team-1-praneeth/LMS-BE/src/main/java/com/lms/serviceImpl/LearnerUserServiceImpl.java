package com.lms.serviceImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lms.entity.LearnerUser;
import com.lms.exception.details.EmailNotFoundException;
import com.lms.repository.LearnerUserRepo;
import com.lms.service.LearnerUserService;

@Service
public class LearnerUserServiceImpl implements LearnerUserService {

	@Autowired
	private LearnerUserRepo lur;

	@Autowired
	private PasswordEncoder pe;

	@Override
	public LearnerUser saveLU(LearnerUser lu) {

		LearnerUser lu1 = new LearnerUser();

		lu1.setName(lu.getName());
		lu1.setEmail(lu.getEmail());
		lu1.setPassword(pe.encode(lu.getPassword()));
		lu1.setRoles(lu.getRoles());

		Boolean byemail = getByemail(lu1);

		if (byemail) {

			return null;
		} else {
			return lur.save(lu1);
		}

	}

	@Override
	public Boolean getByemail(LearnerUser lu) {

		boolean findByName = lur.existsByemail(lu.getEmail());
		return findByName;
	}

	@Override
	public Optional<LearnerUser> fingbyemail(String email) {

		Optional<LearnerUser> findByemail;
		try {
			findByemail = lur.findByemail(email);
			return findByemail;
		} catch (Exception e) {
			throw new EmailNotFoundException("Email not found");
		}

	}

	@Override
	public List<LearnerUser> getLU(long id) {
		return null;
	}

	@Override
	public LearnerUser updateLU(LearnerUser lu) {
		return null;
	}

	@Override
	public void deleteLU(long id) {

	}

	@Override
	public ResponseEntity<?> getby(LearnerUser lu) {

		if (lur.findByemail(lu.getEmail()).isEmpty()) {

			throw new EmailNotFoundException("Email Not Found");
		} else {

			return new ResponseEntity<Object>(lur.findByemail(lu.getEmail()).get(), HttpStatus.OK);
		}

	}

	@Override
	public String saveImg(MultipartFile mp, String email) throws Exception {

		try {

			Optional<LearnerUser> lu = lur.findByemail(email);
			LearnerUser lu1 = lu.get();
			lu1.setImage(compressImage(mp.getBytes()));
			LearnerUser imageData = lur.save(lu1);

			if (imageData != null) {
				return "File Uploaded Successfully :" + mp.getOriginalFilename().toLowerCase();
			} else {
				throw new Exception("Failed To Upload File");
			}
		} catch (Exception e) {

			throw new EmailNotFoundException("Email not Found");
		}

	}

	public static byte[] compressImage(byte[] data) {
		Deflater deflater = new Deflater();
		deflater.setLevel(Deflater.BEST_COMPRESSION);
		deflater.setInput(data);
		deflater.finish();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] tmp = new byte[4 * 1024];

		int compressedSize = 0;
		int targetSize = 1000;

		while (!deflater.finished() && compressedSize < targetSize) {
			int size = deflater.deflate(tmp);
			if (compressedSize + size <= targetSize) {
				outputStream.write(tmp, 0, size);
				compressedSize += size;
			} else {
				break;
			}
		}
		try {
			outputStream.close();
		} catch (Exception ignored) {
		}
		return outputStream.toByteArray();
	}

	@Override
	public byte[] downloadImage(String email) throws IOException, DataFormatException {

		Optional<LearnerUser> img = lur.findByemail(email);

		byte[] images1 = decompressImage(img.get().getImage());

		return images1;
	}

	@Override
	public byte[] decompressImage(byte[] data) {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] tmp = new byte[4 * 1024];
		try {
			while (!inflater.finished()) {
				int count = inflater.inflate(tmp);
				outputStream.write(tmp, 0, count);
			}
			outputStream.close();
		} catch (Exception ignored) {
		}
		return outputStream.toByteArray();
	}

}
