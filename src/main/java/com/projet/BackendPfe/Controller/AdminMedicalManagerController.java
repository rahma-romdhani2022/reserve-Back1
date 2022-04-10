package com.projet.BackendPfe.Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.projet.BackendPfe.Entity.AdminMedicalManager;
import com.projet.BackendPfe.Entity.Expert;
import com.projet.BackendPfe.Entity.Generaliste;
import com.projet.BackendPfe.config.JwtTokenUtil;
import com.projet.BackendPfe.domaine.JwtResponse;
import com.projet.BackendPfe.domaine.Message;
import com.projet.BackendPfe.repository.AdminMedicalManagerRepository;
import com.projet.BackendPfe.repository.UserRepository;
import com.projet.BackendPfe.request.LoginRequest;
import com.projet.BackendPfe.request.RegisterRequestExpert;
import com.projet.BackendPfe.services.AdminMedicalManagerService;
import com.projet.BackendPfe.services.ExpertService;
import com.projet.BackendPfe.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/adminMedical")
public class AdminMedicalManagerController {

	@Autowired 	AuthenticationManager authenticationManager;
	@Autowired	AdminMedicalManagerRepository repository;
	@Autowired	private  AdminMedicalManagerService service  ;
	@Autowired	private ExpertService expertService ;
	@Autowired	AdminMedicalManagerRepository repositoryAdmin;
	@Autowired	PasswordEncoder encoder;
	@Autowired	JwtTokenUtil jwtUtils;


	@PostMapping("/login")
	public ResponseEntity<?> authenticateAdmin(@Valid @RequestBody LoginRequest data) {
		System.out.println(data.getPassword());
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						data.getUsername(),
						data.getPassword()));	
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail()
											));}
	
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerAdmin(@Valid @RequestBody RegisterRequestExpert signUpRequest) {
		if (repository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new Message("Error: Username is already taken!"));	}
		
		if (repository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new Message("Error: Email is already in use!"));
		}
		AdminMedicalManager admin = new AdminMedicalManager(signUpRequest.getUsername(), 
				 signUpRequest.getEmail(),
				 encoder.encode(signUpRequest.getPassword()),
						 signUpRequest.getImage(), new Date(), "Admin Medical Manager");
		repository.save(admin);

		return ResponseEntity.ok(new Message("Admin  registered successfully!"));
	}	  
	
	
	  public ResponseEntity<?> updateAdmin(@PathVariable("id") long id, @RequestBody AdminMedicalManager Admin) {
		    System.out.println("Update Utilisateur with ID = " + id + "...");
		    Optional<AdminMedicalManager> UtilisateurInfo = repository.findById(id);
		    AdminMedicalManager utilisateur = UtilisateurInfo.get();
		    if (repository.existsByUsername(Admin.getUsername())) {
		    	if(repository.findById(id).get().getId() != (repository.findByUsername(Admin.getUsername()).getId())) {
				return ResponseEntity
						.badRequest()
						.body(new Message("Error: Username is already taken!"));
			}
		    }
			if (repository.existsByEmail(Admin.getEmail())) {
				if(repository.findById(id).get().getId() != (repository.findByEmail(Admin.getEmail()).getId())) {
				return ResponseEntity
						.badRequest()
						.body(new Message("Error: Email is already in use!"));
			}
			}


		    	utilisateur.setUsername(Admin.getUsername());
		    	 utilisateur.setEmail(Admin.getEmail());
		    	
		      return new ResponseEntity<>(repository.save(utilisateur), HttpStatus.OK);
		    } 
		  
	  
	  @PutMapping("/updateImage/{id}")
	  public String update(@PathVariable("id") long id ,  @RequestParam("imageFile") MultipartFile imageFile ) throws IOException		    		  {
		  service.updateImage(id,imageFile);
		    	return "Done !!!";
		    }

	/*  public ResponseEntity<AdminMedicalManager> updateAdmin(@PathVariable("id") long id, @RequestBody Generaliste Utilisateur) {
	    System.out.println("Update Utilisateur with ID = " + id + "...");
	 
	    Optional<AdminMedicalManager> UtilisateurInfo = repository.findById(id);

	    AdminMedicalManager utilisateur = UtilisateurInfo.get();
	    	
	    	//utilisateur.setImage(Utilisateur.getImage());       //  utilisateur.getEmail();
	        // utilisateur.getUsername();
	    	
	      return new ResponseEntity<>(repository.save(utilisateur), HttpStatus.OK);
	    } 	
	  @GetMapping( "/getAdmin/{id}" )
		public AdminMedicalManager getAdmin(@PathVariable("id") long id) throws IOException {

		  AdminMedicalManager admin = repository.findById(id).get();
			return admin;
		}*/
	  @PutMapping("/update/{id}")
		 public ResponseEntity<?> updateAdmin(@PathVariable("id") long id, @RequestBody Generaliste Utilisateur) {

	  Optional<AdminMedicalManager> UtilisateurInfo = repository.findById(id);
	  AdminMedicalManager utilisateur = UtilisateurInfo.get();
	    if (repository.existsByUsername(Utilisateur.getUsername())) {
	    	if(repository.findById(id).get().getId() != (repository.findByUsername(Utilisateur.getUsername()).getId())) {
			return ResponseEntity
					.badRequest()
					.body(new Message("Error: Username is already taken!"));
		}
	    }
		if (repository.existsByEmail(Utilisateur.getEmail())) {
			if(repository.findById(id).get().getId() != (repository.findByEmail(Utilisateur.getEmail()).getId())) {
			return ResponseEntity
					.badRequest()
					.body(new Message("Error: Email is already in use!"));
		}
		}

	  
	    	utilisateur.setUsername(Utilisateur.getUsername());
	    	 utilisateur.setEmail(Utilisateur.getEmail());
	
	      return new ResponseEntity<>(repository.save(utilisateur), HttpStatus.OK);
	    } 
	  @GetMapping( "/getImage/{id}" )
		public AdminMedicalManager getImage(@PathVariable("id") long id) throws IOException {

		  AdminMedicalManager admin = repository.findById(id).get();
		  AdminMedicalManager imageAdmin = new AdminMedicalManager(service.decompressZLib(admin.getImage()));
			return imageAdmin;
		}
		@PostMapping("/addExpertParAdmin/{id}")
		public String addExpertParAdmin( @RequestParam("imageFile") MultipartFile imageFile ,@RequestParam("username") String  nom,
				                     @RequestParam("email") String email , @RequestParam("gender") String gender , @RequestParam("telephone") long telephone,
				                     @RequestParam("password")  String password  , @PathVariable("id") long  idAdmin) throws IOException {
		byte[] image = null  ; 
			Expert expert = new Expert(  nom, 
					email, encoder.encode(password),
						gender,telephone,expertService.compressZLib(imageFile.getBytes()),
						new Date() , "expert" ,repository.findById(idAdmin).get());
		
			expertService.ajouterExpert(expert);
			return  "Done !!!!" ;
		}
		@GetMapping("/{id}")
		public AdminMedicalManager get(@PathVariable("id") long  idAdmin) {
			
			AdminMedicalManager admin =  repository.findById(idAdmin).get();
		if(admin.getImage()==null)
			return admin;
		else {
			admin.setImage(service.decompressZLib(admin.getImage()));
			return admin;
		}
		}
 }

