package med.voll.web_application.domain.consulta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    Page<Consulta> findAllByOrderByData(Pageable paginacao);

    Page<Consulta> findByPacienteIdOrderByData(Long id, Pageable paginacao);

    Page<Consulta> findByMedicoIdOrderByData(Long id, Pageable paginacao);

}
