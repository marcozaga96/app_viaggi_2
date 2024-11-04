package marcozagaria.app_viaggi_2.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import marcozagaria.app_viaggi_2.entities.Dipendente;
import marcozagaria.app_viaggi_2.exeption.BadRequestException;
import marcozagaria.app_viaggi_2.exeption.NotFoundException;
import marcozagaria.app_viaggi_2.payloads.DipendenteDTO;
import marcozagaria.app_viaggi_2.repositories.DipendenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;


@Service
public class DipendenteService {
    @Autowired
    private DipendenteRepository dipendenteRepository;

    @Autowired
    private Cloudinary cloudinaryUploader;

    public Page<Dipendente> getAllDipendenteList(int page, int size, String sortBy) {
        if (size > 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return dipendenteRepository.findAll(pageable);
    }

    public Dipendente saveDipendente(DipendenteDTO body) {
        Dipendente newDipendente = new Dipendente(body.nome(), body.cognome(), body.email(), body.username());
        newDipendente.setAvatar("https://ui-avatars.com/api/?name=" + body.nome() + "+" + body.cognome());
        return dipendenteRepository.save(newDipendente);
    }


    public Dipendente cercaId(UUID id) {

        return dipendenteRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Dipendente cercaDipendenteEModifica(UUID id, DipendenteDTO body) {
        Dipendente cerca = cercaId(id);
        cerca.setNome(body.nome());
        cerca.setCognome(body.cognome());
        cerca.setEmail(body.email());
        cerca.setUsername(body.username());
        if (cerca == null) throw new NotFoundException(id);
        return dipendenteRepository.save(cerca);
    }

    public void cercaDipendenteECancella(UUID id) {
        Dipendente cerca = cercaId(id);
        if (cerca == null) throw new NotFoundException(id);
        dipendenteRepository.delete(cerca);
    }

    public String uploadAvatar(MultipartFile file, UUID id) {
        String url = null;
        Dipendente cerca = cercaId(id);
        try {
            url = (String) cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
            cerca.setAvatar(url);
            dipendenteRepository.save(cerca);
        } catch (IOException e) {
            throw new BadRequestException("Ci sono stati problemi con l'upload del file!");
        }
        // ... qua poi dovrei prendere l'url e salvarlo nel rispettivo utente

        return url;
    }
}
