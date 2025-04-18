package com.example

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.html.*
import userCredentials
import users

// Session data class
data class UserSession(val userId: Int) : Principal

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureSecurity()
        configureRouting()
    }.start(wait = true)
}

fun Application.configureSecurity() {
    // Install Sessions
    install(Sessions) {
        cookie<UserSession>("user_session") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60 * 60 // 1 hour
            // In a real app, use a secure secret key and possibly encryption/signing
            // transform(SessionTransportTransformerEncrypt(hex("00112233445566778899aabbccddeeff"), hex("aabbccddeeff0011223344556677")))
        }
    }
    // Install Authentication
    install(Authentication) {
        form("auth-form") {
            userParamName = "username"
            passwordParamName = "password"
            validate { credentials ->
                val username = credentials.name
                val password = credentials.password
                val foundUser = users.values.find { it.username == username }
                if (foundUser != null && userCredentials[username] == password) {
                    UserIdPrincipal(foundUser.id.toString()) // Use UserIdPrincipal for validation phase
                } else {
                    null
                }
            }
            challenge {
                // Redirect to login page on challenge
                call.respondRedirect("/login?error=invalid")
            }
        }

        session<UserSession>("auth-session") {
            validate { session ->
                // Check if user ID from session exists
                if (users.containsKey(session.userId)) session else null
            }
            challenge {
                // Redirect to login page if session is invalid or missing
                call.respondRedirect("/login")
            }
        }
    }
}

fun Application.configureRouting() {
    routing {
        // Login page (GET)
        get("/login") {
            val error = call.request.queryParameters["error"]
            call.respondHtml {
                head {
                    title("Login")
                    // Basic styling using Bootstrap CDN for visual appeal
                    link(
                        rel = "stylesheet",
                        href = "https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
                    )
                }
                body {
                    div(classes = "container mt-5") {
                        h2 { +"Login" }
                        if (error == "invalid") {
                            p(classes = "text-danger") { +"Invalid username or password." }
                        }
                        form(action = "/login", method = FormMethod.post, classes = "needs-validation") {
                            div(classes = "mb-3") {
                                label(classes = "form-label") {
                                    htmlFor = "username"
                                    +"Username"
                                }
                                textInput(name = "username", classes = "form-control") {
                                    id = "username"; required = true
                                }
                            }
                            div(classes = "mb-3") {
                                label(classes = "form-label") {
                                    htmlFor = "password"
                                    +"Password"
                                }
                                passwordInput(name = "password", classes = "form-control") {
                                    id = "password"; required = true
                                }
                            }
                            button(type = ButtonType.submit, classes = "btn btn-primary") { +"Login" }
                        }
                    }
                }
            }
        }
        // Login action (POST) - Protected by form authentication
        authenticate("auth-form") {
            post("/login") {
                val principal = call.principal<UserIdPrincipal>()
                if (principal != null) {
                    val userId = principal.name.toInt()
                    // Set the session cookie upon successful login
                    call.sessions.set(UserSession(userId = userId))
                    // Redirect to the user's own profile page
                    call.respondRedirect("/profile/$userId")
                } else {
                    // Should not happen if validation works, but handle just in case
                    call.respondRedirect("/login?error=unknown")
                }
            }
        }
        // Logout action
        get("/logout") {
            call.sessions.clear<UserSession>()
            call.respondRedirect("/login")
        }
        // Profile page - Protected by session authentication
        authenticate("auth-session") {
            get("/profile/{id}") {
                // --- VULNERABILITY HERE ---
                // It retrieves the user ID from the URL path parameter...
                val requestedUserId = call.parameters["id"]?.toIntOrNull()
                // ...and uses it directly without checking if the logged-in user
                // (from the session) is authorized to view this profile.
                val userSession = call.principal<UserSession>()!! // Assumes session is valid due to authenticate block
                val userToShow = if (requestedUserId != null) users[requestedUserId] else null

                if (userToShow != null) {
                    call.respondHtml {
                        head {
                            title("Profile: ${userToShow.username}")
                            link(
                                rel = "stylesheet",
                                href = "https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
                            )
                        }
                        body {
                            div(classes = "container mt-5") {
                                h2 { +"Profile Page" }
                                hr {}
                                p { strong { +"Viewing Profile ID:" }; +" $requestedUserId" }
                                p { strong { +"Logged in as User ID:" }; +" ${userSession.userId} (${users[userSession.userId]?.username ?: "Unknown"})" }
                                hr {}
                                h3 { +"User Details" }
                                p { strong { +"Username:" }; +" ${userToShow.username}" }
                                p { strong { +"Email:" }; +" ${userToShow.email}" }
                                p { strong { +"Secret Info:" }; i(classes = "text-danger") { +" ${userToShow.secretInfo}" }; +" (This should be protected!)" }
                                hr {}
                                p {
                                    a(href = "/logout", classes = "btn btn-secondary") { +"Logout" }
                                    // Link to potentially view the *other* user's profile (if logged in as alice)
                                    if (userSession.userId == 1) { // Example: Alice is user 1
                                        +" "
                                        a(
                                            href = "/profile/2",
                                            classes = "btn btn-warning ms-2"
                                        ) { +"Try viewing Bob's profile (ID 2)" }
                                    } else if (userSession.userId == 2) { // Example: Bob is user 2
                                        +" "
                                        a(
                                            href = "/profile/1",
                                            classes = "btn btn-warning ms-2"
                                        ) { +"Try viewing Alice's profile (ID 1)" }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                }
            }
        }
        // Redirect root to login
        get("/") {
            call.respondRedirect("/login")
        }
    }
}