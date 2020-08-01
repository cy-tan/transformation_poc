package com.acme.samples.mapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileUploadController {

	@Value("${home.redirect}")
	private String homeURL;

	@Value("${home.ssl}")
	private String homeSSL;

	private final StorageService storageService;

	@Autowired
	public FileUploadController(final StorageService storageService) {
		this.storageService = storageService;
	}

	@GetMapping("/")
	public String listUploadedFiles(final Model model) throws IOException {

		List<String> temp = storageService.loadAll()
				.map(path -> MvcUriComponentsBuilder
						.fromMethodName(FileUploadController.class, "serveFile", path.getFileName().toString()).build()
						.toUri().toString())
				.collect(Collectors.toList());

		List<String> temps = new ArrayList<String>();
		for (String url : temp){
			temps.add(url.replace("http", this.homeSSL));
		}
		//temps.forEach(System.out::println);

		/*model.addAttribute("files",
				storageService.loadAll()
						.map(path -> MvcUriComponentsBuilder
								.fromMethodName(FileUploadController.class, "serveFile", path.getFileName().toString())
								.build().toUri().toString())
						.collect(Collectors.toList()));*/
		model.addAttribute("files", temps);

		return "uploadForm";
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable final String filename) {

		final Resource file = storageService.loadAsResource(filename);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@PostMapping("/")
	public String handleFileUpload(@RequestParam("file") final MultipartFile file,
			@RequestParam("scenario") final String scenario, final RedirectAttributes redirectAttributes,
			HttpServletResponse httpServletResponse) {

		System.out.println("Scenarios selected from web is ===== " + scenario);

		storageService.store(file, scenario);
		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");

		System.out.println("homeURL ================= "+this.homeURL);

		if(this.homeURL.equalsIgnoreCase("/")){
			//httpServletResponse.setHeader("Location", this.homeURL);
			//httpServletResponse.setStatus(200);
			return "redirect:/";
		}

		httpServletResponse.setHeader("Location", this.homeURL);
		httpServletResponse.setStatus(302);
		return null;
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(final StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}


}