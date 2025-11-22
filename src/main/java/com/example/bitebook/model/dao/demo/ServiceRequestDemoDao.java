package com.example.bitebook.model.dao.demo;

import com.example.bitebook.model.Chef;
import com.example.bitebook.model.Client;
import com.example.bitebook.model.ServiceRequest;
import com.example.bitebook.model.dao.ServiceRequestDao;
import com.example.bitebook.model.enums.RequestStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

public class ServiceRequestDemoDao implements ServiceRequestDao {

    // 1. IL "DATABASE" (Tabella ServiceRequest)
    // Key: ID della richiesta (Integer)
    // Value: L'oggetto completo (ServiceRequest)
    private static Map<Integer, ServiceRequest> fakeTable = new HashMap<>();

    // 2. IL GENERATORE DI ID (AUTO_INCREMENT)
    private static int autoIncrementId = 1;


    @Override
    public void saveServiceRequest(ServiceRequest serviceRequest) {
        System.out.println("[Modalità Demo] Salvataggio richiesta in Map...");

        // A. Simula l'assegnazione dell'ID (come il DB)
        serviceRequest.setId(autoIncrementId++);

        // B. Assicuriamoci che lo stato iniziale sia corretto (PENDING)
        if (serviceRequest.getStatus() == null) {
            serviceRequest.setStatus(RequestStatus.PENDING);
        }

        // C. Simula l'INSERT INTO
        // Invece di spacchettare tutti i campi (Date, Time, MenuLevel...),
        // salviamo direttamente l'oggetto intero. È il vantaggio della RAM.
        fakeTable.put(serviceRequest.getId(), serviceRequest);

        System.out.println("[Modalità Demo] Richiesta salvata con ID: " + serviceRequest.getId());
    }


    public Vector<ServiceRequest> getClientServiceRequests(Client client) throws Exception{
//        // Simula: SELECT * FROM ... WHERE clientId = ?
//        // + le JOIN per ottenere i nomi (ma qui abbiamo già gli oggetti completi!)
//
//        return fakeTable.values().stream()
//                // 1. FILTRO (La clausola WHERE)
//                .filter(req -> req.getClient() != null && req.getClient().getId() == client.getId())
//                // 2. RACCOLTA (Mette i risultati in un Vector)
//                .collect(Collectors.toCollection(Vector::new));
//
//        /* NOTA:
//           Nel DB DAO ricostruivi oggetti parziali (new Chef con solo il nome).
//           Qui restituiamo l'oggetto COMPLETO salvato in memoria.
//           Al Controller va benissimo uguale, perché i metodi .getName() funzionano
//           sia su oggetti parziali che completi.
//        */

        // 1. Prepariamo il contenitore per i risultati
        Vector<ServiceRequest> clientRequests = new Vector<>();

        // 2. Iteriamo su tutti i valori salvati nella Mappa
        // fakeTable.values() ci dà la collezione di tutte le ServiceRequest salvate
        for (ServiceRequest req : fakeTable.values()) {

            // 3. Filtriamo: controlliamo se la richiesta appartiene a QUESTO cliente
            // È buona norma controllare che req.getClient() non sia null per evitare crash
            if (req.getClient() != null && req.getClient().getId() == client.getId()) {

                // 4. Se corrisponde, aggiungiamo al vettore
                clientRequests.add(req);
            }
        }

        // 5. Restituiamo il vettore popolato
        return clientRequests;


    }

    @Override
    public Vector<ServiceRequest> getChefServiceRequests(Chef chef) throws Exception {
//        // Simula: SELECT * FROM ... WHERE chefId = ?
//        return fakeTable.values().stream()
//                // 1. FILTRO (La clausola WHERE)
//                .filter(req -> req.getChef() != null && req.getChef().getId() == chef.getId())
//                // 2. RACCOLTA
//                .collect(Collectors.toCollection(Vector::new));

        // 1. Prepariamo il contenitore per i risultati
        Vector<ServiceRequest> chefRequests = new Vector<>();

        // 2. Iteriamo su tutti i valori
        for (ServiceRequest req : fakeTable.values()) {

            // 3. Filtriamo: controlliamo se la richiesta è diretta a QUESTO chef
            if (req.getChef() != null && req.getChef().getId() == chef.getId()) {

                // 4. Se corrisponde, aggiungiamo al vettore
                chefRequests.add(req);
            }
        }

        // 5. Restituiamo il vettore popolato
        return chefRequests;


    }

    @Override
    public void approveRequest(ServiceRequest serviceRequest) throws Exception {
        // Qui replichiamo la logica del tuo DbDao che distingue tra APPROVE e REJECT

        // 1. Cerchiamo la richiesta salvata nel "DB" usando l'ID
        ServiceRequest storedRequest = fakeTable.get(serviceRequest.getId());

        if (storedRequest == null) {
            throw new Exception("Request not found in Demo DB");
        }

        // 2. Controlliamo cosa vuole fare il controller (APPROVE o REJECT)
        // Il controller ti passa un oggetto 'serviceRequest' con lo stato DESIDERATO.

        if (serviceRequest.getStatus().equals(RequestStatus.APPROVED)) {
            // Simula: CALL approveRequest(?)
            // Logica: Si può approvare solo se è PENDING
            if (storedRequest.getStatus() == RequestStatus.PENDING) {
                storedRequest.setStatus(RequestStatus.APPROVED);
                System.out.println("[Modalità Demo] Richiesta " + storedRequest.getId() + " APPROVATA.");
            } else {
                System.out.println("[Modalità Demo] Errore: Impossibile approvare, stato non valido.");
            }

        } else if (serviceRequest.getStatus().equals(RequestStatus.REJECTED)) {
            // Simula: CALL rejectRequest(?)
            // Logica: Si può rifiutare solo se è PENDING (o forse anche APPROVED, dipende dalle regole)
            if (storedRequest.getStatus() == RequestStatus.PENDING) {
                storedRequest.setStatus(RequestStatus.REJECTED);
                System.out.println("[Modalità Demo] Richiesta " + storedRequest.getId() + " RIFIUTATA.");
            } else {
                System.out.println("[Modalità Demo] Errore: Impossibile rifiutare, stato non valido.");
            }

        } else {
            throw new Exception("[Modalità Demo] Invalid request status passed");
        }
    }


}
