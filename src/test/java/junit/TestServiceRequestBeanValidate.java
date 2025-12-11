package junit;


import com.example.bitebook.model.bean.*;
import com.example.bitebook.model.enums.MenuLevel;
import com.example.bitebook.model.enums.RequestStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestServiceRequestBeanValidate{

    private ServiceRequestBean createValidBean() {
        ServiceRequestBean bean = new ServiceRequestBean();

        bean.setClientBean(new ClientBean());
        bean.setChefBean(new ChefBean());
        bean.setMenuBean(new MenuBean());

        bean.setTotalPrice(100); // > 0
        bean.setStatus(RequestStatus.PENDING);

        ReservationDetailsBean rdb = new ReservationDetailsBean();
        rdb.setDate(LocalDate.now().plusDays(10)); // Futuro
        rdb.setTime(LocalTime.of(20, 0));
        rdb.setParticipantNumber(4);
        rdb.setAddress("Via Roma 10, Milano");
        rdb.setSelectedMenuLevel(MenuLevel.BASE);

        bean.setReservationDetailsBean(rdb);

        return bean;
    }


    @Test
    @DisplayName("VALIDATION: Bean completo e corretto -> Ritorna TRUE")
    void testValidate_Success() {
        ServiceRequestBean bean = createValidBean();
        assertTrue(bean.validate(), "Un bean compilato correttamente deve essere valido");
    }


    @Test
    @DisplayName("VALIDATION FAIL: Manca il Client -> Ritorna FALSE")
    void testValidate_NullClient() {
        ServiceRequestBean bean = createValidBean();
        bean.setClientBean(null);

        assertFalse(bean.validate(), "Senza Client il bean non deve essere valido");
    }

    @Test
    @DisplayName("VALIDATION FAIL: Manca lo Chef -> Ritorna FALSE")
    void testValidate_NullChef() {
        ServiceRequestBean bean = createValidBean();
        bean.setChefBean(null);

        assertFalse(bean.validate(), "Senza Chef il bean non deve essere valido");
    }

    @Test
    @DisplayName("VALIDATION FAIL: Manca il Menu -> Ritorna FALSE")
    void testValidate_NullMenu() {
        ServiceRequestBean bean = createValidBean();
        bean.setMenuBean(null);

        assertFalse(bean.validate(), "Senza Menu il bean non deve essere valido");
    }

    @Test
    @DisplayName("VALIDATION FAIL: Prezzo a Zero o Negativo -> Ritorna FALSE")
    void testValidate_InvalidPrice() {
        ServiceRequestBean bean = createValidBean();

        bean.setTotalPrice(0);
        assertFalse(bean.validate(), "Il prezzo non può essere 0");

        bean.setTotalPrice(-50);
        assertFalse(bean.validate(), "Il prezzo non può essere negativo");
    }

    @Test
    @DisplayName("VALIDATION FAIL: Status Mancante -> Ritorna FALSE")
    void testValidate_NullStatus() {
        ServiceRequestBean bean = createValidBean();
        bean.setStatus(null);

        assertFalse(bean.validate(), "Lo stato della richiesta è obbligatorio");
    }

    @Test
    @DisplayName("VALIDATION FAIL: Dettagli Prenotazione Mancanti -> Ritorna FALSE")
    void testValidate_NullReservationDetails() {
        ServiceRequestBean bean = createValidBean();
        bean.setReservationDetailsBean(null);

        assertFalse(bean.validate(), "I dettagli della prenotazione sono obbligatori");
    }

    @Test
    @DisplayName("VALIDATION FAIL: Dettagli Prenotazione Invalidi (Ricorsivo) -> Ritorna FALSE")
    void testValidate_InvalidReservationDetails() {
        ServiceRequestBean bean = createValidBean();

        ReservationDetailsBean badDetails = new ReservationDetailsBean();
        badDetails.setParticipantNumber(-5);

        bean.setReservationDetailsBean(badDetails);

        assertFalse(bean.validate(), "Se i dettagli interni sono invalidi, tutto il bean deve essere invalido");
    }


    @Test
    @DisplayName("VALIDATION: Dati Numerici Invalidi (Prezzo/Partecipanti) -> FALSE")
    void testValidate_InvalidNumbers() {
        ServiceRequestBean bean = createValidBean();

        // Test Prezzo Negativo
        bean.setTotalPrice(-10);
        assertFalse(bean.validate(), "Prezzo negativo non accettabile");

        // Test Partecipanti Zero (Resetto il bean prima)
        bean = createValidBean();
        bean.getReservationDetailsBean().setParticipantNumber(0);
        assertFalse(bean.validate(), "0 Partecipanti non accettabile");
    }

    @Test
    @DisplayName("VALIDATION: Indirizzo troppo corto -> FALSE")
    void testValidate_ShortAddress() {
        ServiceRequestBean bean = createValidBean();
        // Mettiamo un indirizzo di 3 lettere (il minimo è 5)
        bean.getReservationDetailsBean().setAddress("Via");

        assertFalse(bean.validate(), "Indirizzo < 5 caratteri deve essere rifiutato");
    }

}
