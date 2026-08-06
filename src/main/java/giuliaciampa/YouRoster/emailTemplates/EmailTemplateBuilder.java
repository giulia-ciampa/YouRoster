package giuliaciampa.YouRoster.emailTemplates;

public class EmailTemplateBuilder {

    public static String buildAccountApprovalEmail(String userName, String roleName, String officeName, String loginUrl) {
        String officeInfo = (officeName != null && !officeName.isBlank()) ? officeName : "Nessun ufficio specifico";
        return
                """
                        <!DOCTYPE html>
                         <html lang="it">
                         <head>
                         <meta charset="UTF-8">
                         <meta name="viewport" content="width=device-width, initial-scale=1.0">
                         <title>Account Attivato</title>
                         </head>
                         <body style="margin: 0; padding: 0; background-color: #EAEAD7; font-family: Arial, sans-serif; -webkit-text-size-adjust: 100%%; -ms-text-size-adjust: 100%%;">
                        
                         <!-- Layout Container Responsive: Sfondo Beige (#EAEAD7) -->
                         <table border="0" cellpadding="0" cellspacing="0" width="100%%" style="background-color: #EAEAD7; padding: 30px 0;">
                         <tr>
                         <td align="center">
                        
                         <!-- Card Principale su sfondo bianco con bordo #9E4289 -->
                         <table border="0" cellpadding="0" cellspacing="0" width="100%%" style="max-width: 600px; background-color: #ffffff; border-radius: 8px; border: 2px solid #9E4289; overflow: hidden; padding: 30px; box-shadow: 0 4px 6px rgba(0,0,0,0.05);">
                        
                         <!-- Header con brand Ultrasonic Blue (#5D00E0) e bordo Grape Soda -->
                         <tr>
                         <td align="center" style="border-bottom: 2px solid #9E4289; padding-bottom: 15px;">
                         <h1 style="color: #E35226; margin: 0; font-size: 28px; font-weight: bold; letter-spacing: -0.5px;"><span style="color: #9E4289;">You</span>Roster</h1>
                         </td>
                         </tr>
                        
                         <!-- Body con testo Carbon black (#252218) e titolo (#E35226) -->
                         <tr>
                         <td style="padding-top: 25px; color: #33007A; font-size: 15px; line-height: 1.6;">
                         <h2 style="margin-top: 0; color: #E35226; font-size: 20px;">Account Attivato! 🎉</h2>
                         <p style="color: #252218;">Ciao <strong>%s</strong>,</p>
                         <p style="color: #252218;">Il tuo account è stato approvato ed è pronto all'uso.</p>
                        
                         <div style="background-color: #f9f9f6; border-left: 4px solid #5D00E0; padding: 12px 16px; margin: 20px 0; border-radius: 0 6px 6px 0;">
                         <p style="margin: 4px 0; color: #252218;"><strong>Ruolo assegnato:</strong> <span style="color: #5D00E0; font-weight: bold;">%s</span></p>
                         <p style="margin: 4px 0; color: #252218;"><strong>Sede / Ufficio:</strong> <span style="color: #5D00E0;"> %s</span></p>
                         </div>
                         </td>
                         </tr>
                        
                         <!-- Bottone Login in Ultrasonic Blue (#5D00E0) -->
                         <tr>
                         <td align="center" style="padding: 25px 0 15px 0;">
                         <table role="presentation" border="0" cellpadding="0" cellspacing="0">
                         <tr>
                         <td align="center" bgcolor="#5D00E0" style="border-radius: 6px;">
                         <a href="%s" target="_blank" style="font-size: 15px; font-weight: bold; color: #ffffff !important; text-decoration: none; padding: 14px 32px; display: inline-block; border-radius: 6px; background-color: #5D00E0;">LOGIN</a>
                         </td>
                         </tr>
                         </table>
                         </td>
                         </tr>
                        
                         <!-- Footer -->
                         <tr>
                         <td align="center" style="border-top: 1px solid #E3E3CA; padding-top: 15px; font-size: 12px; color: #888888;">
                         <p style="margin: 0;">© YouRoster - Gestione Personale e Turni.</p>
                         </td>
                         </tr>
                        
                         </table>
                        
                         </td>
                         </tr>
                         </table>
                        
                         </body>
                         </html>
                        """.formatted(userName, roleName, officeInfo, loginUrl);
    }

    public static String buildAccountDisableEmail(String userName) {
        return """
                        <!DOCTYPE html>
                         <html lang="it">
                         <head>
                         <meta charset="UTF-8">
                         <meta name="viewport" content="width=device-width, initial-scale=1.0">
                         <title>Account disabilitato</title>
                         </head>
                         <body style="margin: 0; padding: 0; background-color: #EAEAD7; font-family: Arial, sans-serif; -webkit-text-size-adjust: 100%%; -ms-text-size-adjust: 100%%;">
                
                         <!-- Layout Container Responsive: Sfondo Beige (#EAEAD7) -->
                         <table border="0" cellpadding="0" cellspacing="0" width="100%%" style="background-color: #EAEAD7; padding: 30px 0;">
                         <tr>
                         <td align="center">
                
                         <!-- Card Principale su sfondo bianco con bordo #9E4289 -->
                         <table border="0" cellpadding="0" cellspacing="0" width="100%%" style="max-width: 600px; background-color: #ffffff; border-radius: 8px; border: 2px solid #9E4289; overflow: hidden; padding: 30px; box-shadow: 0 4px 6px rgba(0,0,0,0.05);">
                
                         <!-- Header con brand Ultrasonic Blue (#5D00E0) e bordo Grape Soda -->
                         <tr>
                         <td align="center" style="border-bottom: 2px solid #9E4289; padding-bottom: 15px;">
                         <h1 style="color: #E35226; margin: 0; font-size: 28px; font-weight: bold; letter-spacing: -0.5px;"><span style="color: #9E4289;">You</span>Roster</h1>
                         </td>
                         </tr>
                
                         <!-- Body con testo Carbon black (#252218) -->
                         <tr>
                         <td style="padding-top: 25px; color: #33007A; font-size: 15px; line-height: 1.6;">
                         <h2 style="margin-top: 0; color: #252218; font-size: 15px;">Account disabilitato.</h2>
                         <p style="color: #252218;">Ciao <strong>%s</strong>,</p>
                         <p style="color: #252218;">Il tuo account è stato disabilitato, grazie per aver lavorato con noi.</p>
                         </td>
                         </tr>
                
                
                         <!-- Footer -->
                         <tr>
                         <td align="center" style="border-top: 1px solid #E3E3CA; padding-top: 15px; font-size: 12px; color: #888888;">
                         <p style="margin: 0;">© YouRoster - Gestione Personale e Turni.</p>
                         </td>
                         </tr>
                
                         </table>
                
                         </td>
                         </tr>
                         </table>
                
                         </body>
                         </html>
                """.formatted(userName);
    }

    public static String buildAccountRejectionEmail(String email) {
        return
                """
                        <!DOCTYPE html>
                                 <html lang="it">
                                 <head>
                                 <meta charset="UTF-8">
                                 <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                 <title>Account rifiutato</title>
                                 </head>
                                 <body style="margin: 0; padding: 0; background-color: #EAEAD7; font-family: Arial, sans-serif; -webkit-text-size-adjust: 100%%; -ms-text-size-adjust: 100%%;">
                        
                                 <!-- Layout Container Responsive: Sfondo Beige (#EAEAD7) -->
                                 <table border="0" cellpadding="0" cellspacing="0" width="100%%" style="background-color: #EAEAD7; padding: 30px 0;">
                                 <tr>
                                 <td align="center">
                        
                                 <!-- Card Principale su sfondo bianco con bordo #9E4289 -->
                                 <table border="0" cellpadding="0" cellspacing="0" width="100%%" style="max-width: 600px; background-color: #ffffff; border-radius: 8px; border: 2px solid #9E4289; overflow: hidden; padding: 30px; box-shadow: 0 4px 6px rgba(0,0,0,0.05);">
                        
                                 <!-- Header con brand Ultrasonic Blue (#5D00E0) e bordo Grape Soda -->
                                 <tr>
                                 <td align="center" style="border-bottom: 2px solid #9E4289; padding-bottom: 15px;">
                                 <h1 style="color: #E35226; margin: 0; font-size: 28px; font-weight: bold; letter-spacing: -0.5px;"><span style="color: #9E4289;">You</span>Roster</h1>
                                 </td>
                                 </tr>
                        
                                 <!-- Body con testo Carbon black (#252218) -->
                                 <tr>
                                 <td style="padding-top: 25px; color: #33007A; font-size: 15px; line-height: 1.6;">
                                 <h2 style="margin-top: 0; color: #252218; font-size: 15px;">Aggiornamento Dati Sensibili.</h2>
                                 <p style="color: #252218;">Gentile utente, la registrazione con l'email  <strong>%s</strong> non è stata accettata.</p>
                                 <p>Si prega di contattarci telefonicamente</p>
                                 </td>
                                 </tr>
                        
                        
                                 <!-- Footer -->
                                 <tr>
                                 <td align="center" style="border-top: 1px solid #E3E3CA; padding-top: 15px; font-size: 12px; color: #888888;">
                                 <p style="margin: 0;">© YouRoster - Gestione Personale e Turni.</p>
                                 </td>
                                 </tr>
                        
                                 </table>
                        
                                 </td>
                                 </tr>
                                 </table>
                        
                                 </body>
                                 </html>
                        """.formatted(email);
    }


    public static String buildUpdateSensitiveData(String employeeName, String details) {
        return
                """
                         <!DOCTYPE html>
                                         <html lang="it">
                                         <head>
                                         <meta charset="UTF-8">
                                         <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                         <title>Account aggiornato</title>
                                         </head>
                                         <body style="margin: 0; padding: 0; background-color: #EAEAD7; font-family: Arial, sans-serif; -webkit-text-size-adjust: 100%%; -ms-text-size-adjust: 100%%;">
                        
                                         <!-- Layout Container Responsive: Sfondo Beige (#EAEAD7) -->
                                         <table border="0" cellpadding="0" cellspacing="0" width="100%%" style="background-color: #EAEAD7; padding: 30px 0;">
                                         <tr>
                                         <td align="center">
                        
                                         <!-- Card Principale su sfondo bianco con bordo #9E4289 -->
                                         <table border="0" cellpadding="0" cellspacing="0" width="100%%" style="max-width: 600px; background-color: #ffffff; border-radius: 8px; border: 2px solid #9E4289; overflow: hidden; padding: 30px; box-shadow: 0 4px 6px rgba(0,0,0,0.05);">
                        
                                         <!-- Header con brand Ultrasonic Blue (#5D00E0) e bordo Grape Soda -->
                                         <tr>
                                         <td align="center" style="border-bottom: 2px solid #9E4289; padding-bottom: 15px;">
                                         <h1 style="color: #E35226; margin: 0; font-size: 28px; font-weight: bold; letter-spacing: -0.5px;"><span style="color: #9E4289;">You</span>Roster</h1>
                                         </td>
                                         </tr>
                        
                                         <!-- Body con testo Carbon black (#252218) -->
                                         <tr>
                                         <td style="padding-top: 25px; color: #33007A; font-size: 15px; line-height: 1.6;">
                                         <h2 style="margin-top: 0; color: #252218; font-size: 15px;">Aggiornamento Dati Sensibili.</h2>
                                         <p style="color: #252218;">L'utente <strong>%s</strong> ha appena aggiornato il/la <strong>%s</strong> nel proprio profilo.</p>
                                         <p>Si prega di accedere a YouRoster per verificare i nuovi dati.</p>
                                         </td>
                                         </tr>
                        
                        
                                         <!-- Footer -->
                                         <tr>
                                         <td align="center" style="border-top: 1px solid #E3E3CA; padding-top: 15px; font-size: 12px; color: #888888;">
                                         <p style="margin: 0;">© YouRoster - Gestione Personale e Turni.</p>
                                         </td>
                                         </tr>
                        
                                         </table>
                        
                                         </td>
                                         </tr>
                                         </table>
                        
                                         </body>
                                         </html>
                        """.formatted(employeeName, details);
    }
}
      
