//ESTE PROYECTO SE CONFIGURA A TRAVÉS DE SPRING INITIALIZR, SELECCIONANDO LAS DEPENDENCIAS NECESARIAS:
//****************************************************************************************************
//SPRING WEB, SPRING DATA JPA, MYSQL DRIVER, SPRING BOOT STARTER VALIDATION, SPRING SECURITY
//******************************************************************************************
//SPRINGFOX SWAGGER2 Y SPRINGFOX SWAGGER UI. UNA VEZ DESCARGADO Y DESCOMPRIMIDO EL PROYECTO
//*****************************************************************************************
//SE PUEDE PROCEDER CON LA IMPLEMENTACIÓN DE LA API
//*************************************************


//CRUD MODELO CLIENTE
//*******************
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


//LA ENTIDAD Cliente REPRESENTA LA ESTRUCTURA DE LA TABLA "Cliente" EN LA BASE DE DATOS
//*************************************************************************************
@Entity
public class Cliente {
    
    //ID ES LA CLAVE PRIMARIA, GENERADA AUTOMÁTICAMENTE
    //*************************************************
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //EL NOMBRE DEL CLIENTE, CAMPO OBLIGATORIO
    //****************************************
    @NotBlank
    private String nombre;

    //EL APELLIDO DEL CLIENTE, CAMPO OBLIGATORIO
    //******************************************
    @NotBlank
    private String apellido;

    //EL EMAIL DEL CLIENTE, CAMPO OBLIGATORIO Y DEBE SER UN EMAIL VÁLIDO
    //*******************************************************************
    @Email
    @NotBlank
    private String email;

    //EL TELÉFONO DEL CLIENTE
    //***********************
    private String telefono;

    //GETTERS Y SETTERS PARA ACCEDER Y MODIFICAR LOS CAMPOS PRIVADOS
    //**************************************************************
}


//REPOSITORIO
//***********
import org.springframework.data.jpa.repository.JpaRepository;

//REPOSITORIO Cliente PARA REALIZAR OPERACIONES CRUD EN LA BASE DE DATOS
//**********************************************************************
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}


//SERVICIO
//********
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

//SERVICIO CLIENTE PARA CONTENER LA LÓGICA DE NEGOCIO
//***************************************************

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    //MÉTODO PARA OBTENER TODOS LOS CLIENTES
    //**************************************
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    //MÉTODO PARA OBTENER UN CLIENTE POR ID
    //*************************************
    public Cliente findById(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    //MÉTODO PARA GUARDAR UN CLIENTE
    //******************************
    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    //MÉTODO PARA ELIMINAR UN CLIENTE POR ID
    //**************************************
    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }
}


//CONTROLADOR
//***********
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

//CONTROLADOR Cliente PARA MANEJAR SOLICITUDES HTTP
//*************************************************
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    //Endpoint PARA OBTENER TODOS LOS CLIENTES
    //****************************************
    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteService.findAll();
    }

    //Endpoint PARA OBTENER UN CLIENTE POR ID
    //***************************************
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long id) {
        Cliente cliente = clienteService.findById(id);
        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente);
    }

    //Endpoint PARA CREAR UN NUEVO CLIENTE
    //************************************
    @PostMapping
    public Cliente createCliente(@Valid @RequestBody Cliente cliente) {
        return clienteService.save(cliente);
    }

    //Endpoint PARA ACTUALIZAR UN CLIENTE EXISTENTE
    //*********************************************
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @Valid @RequestBody Cliente clienteDetails) {
        Cliente cliente = clienteService.findById(id);
        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }

        cliente.setNombre(clienteDetails.getNombre());
        cliente.setApellido(clienteDetails.getApellido());
        cliente.setEmail(clienteDetails.getEmail());
        cliente.setTelefono(clienteDetails.getTelefono());

        Cliente updatedCliente = clienteService.save(cliente);
        return ResponseEntity.ok(updatedCliente);
    }

    //Endpoint PARA ELIMINAR UN CLIENTE POR ID
    //****************************************
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        Cliente cliente = clienteService.findById(id);
        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }

        clienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

//LA CONFIGURACIÓN DE SPRING BOOT SE REALIZA AUTOMÁTICAMENTE GRACIAS A LAS ANOTACIONES
//************************************************************************************
//@SPRINGBOOTAPPLICATION Y @ENABLEAUTOCONFIGURATION EN LA CLASE PRINCIPAL DEL PROYECTO
//**************************************************************************************


//LOS endpoints PARA LAS OPERACIONES CRUD ESTÁN DEFINIDOS EN EL CONTROLADOR ClienteController
//*******************************************************************************************
//ESTOS endpoints PERMITEN INTERACTUAR CON LA ENTIDAD Cliente A TRAVÉS DE SOLICITUDES HTTP
//****************************************************************************************


//CONFIGURACIÓN
//**************

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//CONFIGURACIÓN DE Swagger PARA LA DOCUMENTACIÓN DE LA API
//********************************************************
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    
    //CONFIGURACIÓN DEL Docket de Swagger PARA ESCANEAR LOS CONTROLADORES Y GENERAR LA DOCUMENTACIÓN
    //**********************************************************************************************
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.tu.paquete"))
                .paths(PathSelectors.any())
                .build();
    }
}


//CONFIGURACIÓN DE SPRING SECURITY
//********************************
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//CONFIGURACIÓN DE SPRING SECURITY PARA PROTEGER LOS endpoints
//************************************************************
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/api/clientes/**").hasRole("USER")
            .anyRequest().authenticated()
            .and()
            .httpBasic();
    }

    //BEAN PARA LA CODIFICACIÓN DE CONTRASEÑAS
    //****************************************
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


//INTEGRACIÓN DEL SERVICIO CLIMA
//*******************************
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//SERVICIO WeatherService PARA INTEGRAR UNA API DE CLIMA EXTERNA
//**************************************************************
@Service
public class WeatherService {

    private final String API_KEY = "your_api_key";
    private final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";

    //MÉTODO PARA OBTENER EL CLIMA DE UNA CIUDAD ESPECÍFICA
    //*****************************************************
    public String getWeather(String city) {
        RestTemplate restTemplate = new RestTemplate();
        String url = BASE_URL + "?q=" + city + "&appid=" + API_KEY;
        return restTemplate.getForObject(url, String.class);
    }
}

//ENDPOINT PARA OBTENER EL CLIMA
//******************************
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


//CONTROLADOR WeatherController PARA MANEJAR SOLICITUDES RELACIONADAS CON EL CLIMA
//********************************************************************************
@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    // Endpoint para obtener el clima de una ciudad.
    @GetMapping
    public String getWeather(@RequestParam String city) {
        return weatherService.getWeather(city);
    }
}
