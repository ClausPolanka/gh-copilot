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
    // Install Sessions (Same as before)
    install(Sessions) {
        cookie<UserSession>("user_session") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60 * 60 // 1 hour
            // In a real app, use a secure secret key and possibly encryption/signing
        }
    }
    // Install Authentication (Same as before)
    install(Authentication) {
        form("auth-form") {
            userParamName = "username"
            passwordParamName = "password"
            validate { credentials ->
                val username = credentials.name
                val password = credentials.password
                val foundUser = users.values.find { it.username == username }
                if (foundUser != null && userCredentials[username] == password) {
                    UserIdPrincipal(foundUser.id.toString())
                } else {
                    null
                }
            }
            challenge {
                call.respondRedirect("/login?error=invalid")
            }
        }

        session<UserSession>("auth-session") {
            validate { session ->
                if (users.containsKey(session.userId)) session else null
            }
            challenge {
                call.respondRedirect("/login")
            }
        }
    }
}

fun Application.configureRouting() {
    routing {
        // Login page (GET) - (Mostly the same, just updated title)
        get("/login") {
            val error = call.request.queryParameters["error"]
            call.respondHtml {
                head {
                    title("Login - Vulnerable App")
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
                                label(classes = "form-label") { htmlFor = "username"; +"Username" }
                                textInput(name = "username", classes = "form-control") {
                                    id = "username"; required = true
                                }
                            }
                            div(classes = "mb-3") {
                                label(classes = "form-label") { htmlFor = "password"; +"Password" }
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
        // Login action (POST) - Redirect to dashboard
        authenticate("auth-form") {
            post("/login") {
                val principal = call.principal<UserIdPrincipal>()
                if (principal != null) {
                    val userId = principal.name.toInt()
                    call.sessions.set(UserSession(userId = userId))
                    // --- CHANGE: Redirect to dashboard instead of profile ---
                    call.respondRedirect("/dashboard")
                } else {
                    call.respondRedirect("/login?error=unknown")
                }
            }
        }
        // Logout action (Same as before)
        get("/logout") {
            call.sessions.clear<UserSession>()
            call.respondRedirect("/login")
        }
        // --- NEW: Dashboard page ---
        authenticate("auth-session") {
            get("/dashboard") {
                val userSession = call.principal<UserSession>()!!
                val currentUser = users[userSession.userId]

                call.respondHtml {
                    head {
                        title("Dashboard")
                        link(
                            rel = "stylesheet",
                            href = "https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
                        )
                    }
                    body {
                        div(classes = "container mt-5") {
                            h2 { +"Dashboard" }
                            if (currentUser != null) {
                                p { +"Welcome, ${currentUser.username}!" }
                            } else {
                                p { +"Welcome!" } // Should not happen if session is valid
                            }
                            hr {}
                            h3 { +"View Profile" }
                            // Form to view the user's OWN profile (using hidden input or just pre-filled)
                            form(action = "/profile", method = FormMethod.get) {
                                // We use a hidden field here. An attacker could change this value
                                // using browser developer tools before submitting the form.
                                hiddenInput(name = "uid") { value = userSession.userId.toString() }
                                button(type = ButtonType.submit, classes = "btn btn-primary") { +"View My Profile" }
                            }
                            // Add a direct link/button to demonstrate the exploit easily
                            if (currentUser != null) {
                                val otherUserId = if (currentUser.id == 1) 2 else 1
                                val otherUsername = users[otherUserId]?.username ?: "Other User"
                                div(classes = "mt-3") {
                                    p { +"Try accessing another profile directly:" }
                                    // This link directly exposes the vulnerable parameter in the URL
                                    a(
                                        href = "/profile?uid=$otherUserId",
                                        classes = "btn btn-warning"
                                    ) { +"Attempt to View $otherUsername's Profile (ID: $otherUserId)" }
                                }
                            }
                            hr {}
                            p { a(href = "/logout", classes = "btn btn-secondary mt-3") { +"Logout" } }
                        }
                    }
                }
            }
        }
        // Profile page - Accessed via query parameter `uid`
        authenticate("auth-session") {
            // --- CHANGE: Route path and parameter access ---
            get("/profile") { // Changed from /profile/{id}
                // --- VULNERABILITY HERE ---
                // It retrieves the user ID from the query parameter 'uid'...
                val requestedUserId = call.request.queryParameters["uid"]?.toIntOrNull()
                // ...and uses it directly without checking if the logged-in user
                // (from the session) is authorized to view this profile.
                val userSession = call.principal<UserSession>()!!
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
                                p { strong { +"Viewing Profile for User ID (from uid parameter):" }; +" $requestedUserId" }
                                p { strong { +"Logged in as User ID:" }; +" ${userSession.userId} (${users[userSession.userId]?.username ?: "Unknown"})" }
                                hr {}
                                h3 { +"User Details" }
                                p { strong { +"Username:" }; +" ${userToShow.username}" }
                                p { strong { +"Email:" }; +" ${userToShow.email}" }
                                p { strong { +"Secret Info:" }; i(classes = "text-danger") { +" ${userToShow.secretInfo}" }; +" (This should be protected!)" }
                                hr {}
                                p {
                                    a(href = "/dashboard", classes = "btn btn-secondary") { +"Back to Dashboard" }
                                    +" "
                                    a(href = "/logout", classes = "btn btn-danger ms-2") { +"Logout" }
                                }
                            }
                        }
                    }
                } else {
                    call.respondHtml(HttpStatusCode.NotFound) {
                        head {
                            title("Not Found")
                            link(
                                rel = "stylesheet",
                                href = "https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
                            )
                        }
                        body {
                            div(classes = "container mt-5") {
                                h2 { +"User Not Found" }
                                p { +"The requested user ID ($requestedUserId) does not exist." }
                                a(href = "/dashboard", classes = "btn btn-secondary mt-3") { +"Back to Dashboard" }
                            }
                        }
                    }
                }
            }
        }
        // Redirect root to login (Same as before)
        get("/") {
            call.respondRedirect("/login")
        }
    }
}