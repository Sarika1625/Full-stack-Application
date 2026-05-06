package com.example.demo;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; 
import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
@Controller
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    // --- LANDING PAGE ---
    @GetMapping("/")
    public String landing() {
        return "index"; 
    }

    // --- ADMIN SIDE ---
    
    @GetMapping("/admin")
    public String adminDashboard(HttpSession session, Model model) {
        // 1. Check if the user is authorized as an ADMIN
        if (!"ADMIN".equals(session.getAttribute("role"))) {
            // If not, redirect them to the admin login page
            return "redirect:/admin/login"; 
        }
        
        // 2. If authorized, load the data as usual
        model.addAttribute("events", eventRepository.findAll());
        model.addAttribute("tickets", ticketRepository.findAll());
        
        return "admin-dashboard";
    }

    @GetMapping("/admin/create-event")
    public String createEventPage(Model model) {
        model.addAttribute("event", new Event());
        return "create-event";
    }

    @PostMapping("/admin/save-event")
    public String saveEvent(@ModelAttribute("event") Event event, RedirectAttributes ra) {
        // Business Logic: Initial seats = Total seats
        event.setAvailableSeats(event.getTotalSeats());
        event.setStatus("PUBLISHED");
        eventRepository.save(event);
        
        // Use 'successMessage' to match your admin-dashboard.html logic
        ra.addFlashAttribute("successMessage", "Event '" + event.getName() + "' created successfully!");
        return "redirect:/admin";
    }

    @GetMapping("/admin/delete-event/{id}")
    public String deleteEvent(@PathVariable Long id, RedirectAttributes ra) {
        // Cascading Delete: Manually clean up tickets to avoid FK constraint errors
        List<Ticket> tickets = ticketRepository.findAll();
        for (Ticket t : tickets) {
            if (t.getEventId() != null && t.getEventId().equals(id)) {
                ticketRepository.delete(t);
            }
        }
        eventRepository.deleteById(id);
        ra.addFlashAttribute("successMessage", "Event and all related bookings removed.");
        return "redirect:/admin";
    }

    @GetMapping("/admin/clear-all")
    public String clearAll(RedirectAttributes ra) {
        ticketRepository.deleteAll();
        eventRepository.deleteAll();
        ra.addFlashAttribute("successMessage", "System reset: All data cleared freshly.");
        return "redirect:/admin";
    }

    // --- USER SIDE ---

    @GetMapping("/user")
    public String userDashboard(HttpSession session, Model model) {
        // 1. Security Check: If no user is in the session, go to login page
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/user/login"; 
        }
        
        // 2. Fetch data only if logged in
        List<Event> events = eventRepository.findAll();
        model.addAttribute("events", events);
        
        // Fetch tickets specifically for the logged-in user if needed
        List<Ticket> tickets = ticketRepository.findAll(); 
        model.addAttribute("tickets", tickets);
        
        return "user-dashboard"; // Points to user-dashboard.html
    }

    @GetMapping("/user/book-event/{id}")
    public String bookEventPage(@PathVariable Long id, Model model) {
        Event selectedEvent = eventRepository.findById(id).orElse(null);
        if (selectedEvent == null) return "redirect:/user";

        model.addAttribute("event", selectedEvent);
        Ticket newTicket = new Ticket();
        newTicket.setEventId(id); 
        model.addAttribute("ticket", newTicket);
        return "book-event";
    }

 // 1. CONFIRM BOOKING (Sets the name and venue from the Event)
    @PostMapping("/user/confirm-booking")
    public String confirmBooking(@ModelAttribute("ticket") Ticket ticket, RedirectAttributes ra) {
        Event event = eventRepository.findById(ticket.getEventId()).orElse(null);
        
        if (event != null && event.getAvailableSeats() >= ticket.getQuantity()) {
            event.setAvailableSeats(event.getAvailableSeats() - ticket.getQuantity());
            eventRepository.save(event);

            // POPULATE THE NEW FIELDS
            ticket.setEventName(event.getName());
            ticket.setVenue(event.getVenue());
            ticket.setTotalPrice(event.getTicketPrice() * ticket.getQuantity());
            ticket.setStatus("CONFIRMED");
            
            ticketRepository.save(ticket);
            return "redirect:/booking-success";
        }
        return "redirect:/user";
    }

    // 2. CANCEL BOOKING (Returns seats to the Event)
    @PostMapping("/user/cancel-booking")
    public String cancelBooking(@RequestParam Long ticketId, @RequestParam int cancelQuantity, RedirectAttributes ra) {
        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
        if (ticket == null) return "redirect:/user";

        Event event = eventRepository.findById(ticket.getEventId()).orElse(null);
        if (event != null && cancelQuantity <= ticket.getQuantity()) {
            // Return seats to pool
            event.setAvailableSeats(event.getAvailableSeats() + cancelQuantity);
            eventRepository.save(event);

            if (cancelQuantity == ticket.getQuantity()) {
                ticketRepository.delete(ticket);
            } else {
                ticket.setQuantity(ticket.getQuantity() - cancelQuantity);
                ticket.setTotalPrice(ticket.getQuantity() * event.getTicketPrice());
                ticketRepository.save(ticket);
            }
            ra.addFlashAttribute("success", "Cancelled " + cancelQuantity + " tickets.");
        }
        return "redirect:/user";
    }
    @GetMapping("/booking-success")
    public String successPage() {
        return "booking-success";
    }
}