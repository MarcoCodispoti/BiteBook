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
        rdb.setDate(LocalDate.now().plusDays(10)); // Future date
        rdb.setTime(LocalTime.of(20, 0));
        rdb.setParticipantNumber(4);
        rdb.setAddress("Via Roma 10, Milano");
        rdb.setSelectedMenuLevel(MenuLevel.BASE);

        bean.setReservationDetailsBean(rdb);

        return bean;
    }


    @Test
    @DisplayName("VALIDATION: Complete and correct Bean -> Returns TRUE")
    void testValidate_Success() {
        ServiceRequestBean bean = createValidBean();
        assertTrue(bean.validate(), "A correctly populated bean must be valid");
    }


    @Test
    @DisplayName("VALIDATION FAIL: Missing Client -> Returns FALSE")
    void testValidate_NullClient() {
        ServiceRequestBean bean = createValidBean();
        bean.setClientBean(null);

        assertFalse(bean.validate(), "Without Client the bean must not be valid");
    }


    @Test
    @DisplayName("VALIDATION FAIL: Missing Chef -> Returns FALSE")
    void testValidate_NullChef() {
        ServiceRequestBean bean = createValidBean();
        bean.setChefBean(null);

        assertFalse(bean.validate(), "Without Chef the bean must not be valid");
    }


    @Test
    @DisplayName("VALIDATION FAIL: Missing Menu -> Returns FALSE")
    void testValidate_NullMenu() {
        ServiceRequestBean bean = createValidBean();
        bean.setMenuBean(null);

        assertFalse(bean.validate(), "Without Menu the bean must not be valid");
    }


    @Test
    @DisplayName("VALIDATION FAIL: Zero or Negative Price -> Returns FALSE")
    void testValidate_InvalidPrice() {
        ServiceRequestBean bean = createValidBean();

        bean.setTotalPrice(0);
        assertFalse(bean.validate(), "Price cannot be 0");

        bean.setTotalPrice(-50);
        assertFalse(bean.validate(), "Price cannot be negative");
    }


    @Test
    @DisplayName("VALIDATION FAIL: Missing Status -> Returns FALSE")
    void testValidate_NullStatus() {
        ServiceRequestBean bean = createValidBean();
        bean.setStatus(null);

        assertFalse(bean.validate(), "Request status is mandatory");
    }


    @Test
    @DisplayName("VALIDATION FAIL: Missing Reservation Details -> Returns FALSE")
    void testValidate_NullReservationDetails() {
        ServiceRequestBean bean = createValidBean();
        bean.setReservationDetailsBean(null);

        assertFalse(bean.validate(), "Reservation details are mandatory");
    }


    @Test
    @DisplayName("VALIDATION FAIL: Invalid Reservation Details (Recursive) -> Returns FALSE")
    void testValidate_InvalidReservationDetails() {
        ServiceRequestBean bean = createValidBean();

        ReservationDetailsBean badDetails = new ReservationDetailsBean();
        badDetails.setParticipantNumber(-5);

        bean.setReservationDetailsBean(badDetails);

        assertFalse(bean.validate(), "If internal details are invalid, the whole bean must be invalid");
    }


    @Test
    @DisplayName("VALIDATION: Invalid Numeric Data (Price/Participants) -> FALSE")
    void testValidate_InvalidNumbers() {
        ServiceRequestBean bean = createValidBean();

        // Test Negative Price
        bean.setTotalPrice(-10);
        assertFalse(bean.validate(), "Negative price not acceptable");

        // Test Zero Participants (Reset bean first)
        bean = createValidBean();
        bean.getReservationDetailsBean().setParticipantNumber(0);
        assertFalse(bean.validate(), "0 Participants not acceptable");
    }


    @Test
    @DisplayName("VALIDATION: Address too short -> FALSE")
    void testValidate_ShortAddress() {
        ServiceRequestBean bean = createValidBean();
        // Set an address with 3 letters (minimum is 5)
        bean.getReservationDetailsBean().setAddress("Via");

        assertFalse(bean.validate(), "Address < 5 characters must be rejected");
    }


}
