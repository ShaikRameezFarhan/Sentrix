package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ai.IrisChatMessage
import com.example.data.ai.IrisCopilotService
import com.example.data.model.AppDetection
import com.example.data.model.BrandProfile
import com.example.data.model.SocialDetection
import com.example.ui.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun IrisCopilotScreen(
    brandProfile: BrandProfile,
    irisService: IrisCopilotService,
    initialThreatSocial: SocialDetection? = null,
    initialThreatApp: AppDetection? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var activeSubMode by remember { mutableStateOf("chat") } // "chat" or "video"

    var inputMessage by remember { mutableStateOf("") }
    var isThinking by remember { mutableStateOf(false) }

    // Video Forensic State (gemini-3.1-pro-preview)
    var videoTitle by remember { mutableStateOf("${brandProfile.name} CEO VIP High-Yield Crypto Deepfake") }
    var videoPlatform by remember { mutableStateOf("Instagram Reels") }
    var videoDescription by remember { mutableStateOf("Video features synthetic voice clone of executive urging viewers to deposit funds into an unverified micro-investment scheme.") }
    var videoTranscript by remember { mutableStateOf("Hello, I am personally guaranteeing all depositors in this special ${brandProfile.name} promo double returns within 24 hours. Click the bio link now.") }
    var customVideoQuery by remember { mutableStateOf("Identify extracted claims, synthetic audio markers, and draft an immediate DMCA/abuse report.") }
    var videoReportResult by remember { mutableStateOf<String?>(null) }
    var isAnalyzingVideo by remember { mutableStateOf(false) }

    val chatMessages = remember {
        mutableStateListOf(
            IrisChatMessage(
                id = "init_1",
                sender = "iris",
                content = """
                    Welcome back, Lead Analyst. I am IRIS, your autonomous Digital Risk Protection copilot developed by Team CIPHER.
                    
                    I am actively monitoring ${brandProfile.name}'s digital brand perimeter. I can explain risk scoring models, analyze homoglyph look-alike patterns, inspect video deepfakes with Gemini Pro (gemini-3.1-pro-preview), and generate formal platform takedown notices.
                    
                    How can I assist your investigation today?
                """.trimIndent(),
                timestamp = SimpleDateFormat("HH:mm", Locale.US).format(Date()),
                suggestedActions = listOf(
                    "Why was @ApexB0nk_Support flagged?",
                    "Draft platform takedown request",
                    "Explain rogue app permissions",
                    "Summarize active investigation"
                )
            )
        )
    }

    // Auto-prompt if navigated with specific threat context
    LaunchedEffect(initialThreatSocial, initialThreatApp) {
        if (initialThreatSocial != null) {
            val autoPrompt = "Explain risk signals and draft takedown for @${initialThreatSocial.username}"
            inputMessage = autoPrompt
        } else if (initialThreatApp != null) {
            val autoPrompt = "Analyze developer mismatch and dangerous permissions for ${initialThreatApp.appName}"
            inputMessage = autoPrompt
        }
    }

    fun sendMessage(promptText: String) {
        val trimmed = promptText.trim()
        if (trimmed.isEmpty()) return

        val userMsg = IrisChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            sender = "user",
            content = trimmed,
            timestamp = SimpleDateFormat("HH:mm", Locale.US).format(Date())
        )
        chatMessages.add(userMsg)
        inputMessage = ""
        isThinking = true

        coroutineScope.launch {
            listState.animateScrollToItem(chatMessages.size - 1)
            val reply = irisService.askIris(trimmed, brandProfile, initialThreatSocial, initialThreatApp)
            val irisMsg = IrisChatMessage(
                id = "msg_iris_${System.currentTimeMillis()}",
                sender = "iris",
                content = reply,
                timestamp = SimpleDateFormat("HH:mm", Locale.US).format(Date())
            )
            chatMessages.add(irisMsg)
            isThinking = false
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // IRIS Persona Header with ZeroFox Aesthetics
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SurfaceCard)
                .border(1.dp, VividLime.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Stylized IRIS Avatar with Vivid Lime
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(VividLimeDark)
                        .border(1.5.dp, VividLime, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "IRIS Persona",
                        tint = VividLime,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "IRIS AI SECURITY COPILOT",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(SafeEmeraldBg)
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "ACTIVE",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = SafeEmerald
                            )
                        }
                    }
                    Text(
                        text = "Autonomous Neural Risk Investigator • Team CIPHER",
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                    Text(
                        text = "Tenant: ${brandProfile.name} • Engine: gemini-3.1-pro-preview",
                        fontSize = 10.sp,
                        color = ElectricCyan
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Tool Mode Switcher: Copilot Chat vs Video Forensic Analyzer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { activeSubMode = "chat" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activeSubMode == "chat") VividLime else SurfaceCard,
                    contentColor = if (activeSubMode == "chat") PrimaryDark else TextWhite
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Copilot Chat", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { activeSubMode = "video" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activeSubMode == "video") ElectricCyan else SurfaceCard,
                    contentColor = if (activeSubMode == "video") PrimaryDark else TextWhite
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.VideoLibrary, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Video Forensics", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (activeSubMode == "chat") {
            // Suggested Prompt Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf(
                    "Why was @ApexB0nk_Support flagged?",
                    "Draft platform takedown request",
                    "Explain rogue app permissions",
                    "Summarize investigation",
                    "Check look-alike variations"
                ).forEach { prompt ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(SecondaryDark)
                            .border(0.5.dp, SurfaceBorderLight, RoundedCornerShape(20.dp))
                            .clickable { sendMessage(prompt) }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(prompt, fontSize = 11.sp, color = ElectricCyan)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Chat Message Thread
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(chatMessages) { msg ->
                    ChatMessageBubble(msg = msg, onCopy = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("IRIS Analysis", msg.content))
                        Toast.makeText(context, "Copied analysis to clipboard!", Toast.LENGTH_SHORT).show()
                    })
                }

                if (isThinking) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceCard)
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            CircularProgressIndicator(
                                color = VividLime,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "IRIS is analyzing threat telemetry and legal rules...",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Chat Input Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputMessage,
                    onValueChange = { inputMessage = it },
                    placeholder = { Text("Ask IRIS to analyze risk, draft takedowns, or inspect look-alikes...", fontSize = 11.sp, color = TextSubtle) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("iris_chat_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceCard,
                        unfocusedContainerColor = SurfaceCard,
                        focusedBorderColor = VividLime,
                        unfocusedBorderColor = SurfaceBorder,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { sendMessage(inputMessage) },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(VividLime)
                        .testTag("iris_send_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send to IRIS",
                        tint = PrimaryDark,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        } else {
            // Video Understanding Workspace (gemini-3.1-pro-preview)
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(SurfaceCard)
                            .border(1.dp, SurfaceBorder, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.VideoLibrary, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("VIDEO & DEEPFAKE THREAT FORENSICS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ElectricCyan)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Inspect social reels, shorts, and video advertisements for deepfake audio, unauthorized executive likeness, and rogue download QR codes using Gemini Pro (gemini-3.1-pro-preview).",
                                fontSize = 11.sp,
                                color = TextMuted,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }

                // Presets
                item {
                    Text("Select Suspicious Video Threat:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        PresetVideoChip(
                            label = "CEO Voice Deepfake Reel",
                            platform = "Instagram",
                            onClick = {
                                videoTitle = "${brandProfile.name} CEO AI Voice Deepfake: High-Yield Scam"
                                videoPlatform = "Instagram Reels"
                                videoDescription = "Deceptive video featuring cloned executive voice and unauthorized ${brandProfile.name} logo encouraging users to deposit into unregulated wallet."
                                videoTranscript = "Hello everyone, this is an official ${brandProfile.name} investment opportunity. Double your funds within 24 hours via our bio link."
                            }
                        )
                        PresetVideoChip(
                            label = "Deep Discount Fraud Ad",
                            platform = "TikTok",
                            onClick = {
                                videoTitle = "${brandProfile.name} Flash Sale VIP Voucher Scam"
                                videoPlatform = "TikTok Ad"
                                videoDescription = "Counterfeit video ad using copied brand marketing assets directing viewers to credential-harvesting phishing site."
                                videoTranscript = "Exclusive secret sale for ${brandProfile.name}! Download our companion APK from the link to claim your voucher."
                            }
                        )
                        PresetVideoChip(
                            label = "Rogue APK Installation Guide",
                            platform = "YouTube Shorts",
                            onClick = {
                                videoTitle = "How to install ${brandProfile.name} MicroLoan Modded APK"
                                videoPlatform = "YouTube Shorts"
                                videoDescription = "Tutorial video distributing Trojanized APK with overlay permissions and SMS interception capability."
                                videoTranscript = "Follow this guide to get unlimited credit with ${brandProfile.name}. Download from the pinned comment mirror."
                            }
                        )
                    }
                }

                // Inputs
                item {
                    OutlinedTextField(
                        value = videoTitle,
                        onValueChange = { videoTitle = it },
                        label = { Text("Video Title / Caption", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricCyan,
                            unfocusedBorderColor = SurfaceBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedContainerColor = SurfaceCard,
                            unfocusedContainerColor = SurfaceCard
                        ),
                        singleLine = true
                    )
                }

                item {
                    OutlinedTextField(
                        value = videoTranscript,
                        onValueChange = { videoTranscript = it },
                        label = { Text("Speech Audio Transcript / Spoken Claims", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricCyan,
                            unfocusedBorderColor = SurfaceBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedContainerColor = SurfaceCard,
                            unfocusedContainerColor = SurfaceCard
                        ),
                        maxLines = 3
                    )
                }

                item {
                    Button(
                        onClick = {
                            isAnalyzingVideo = true
                            videoReportResult = null
                            coroutineScope.launch {
                                val report = irisService.analyzeVideoThreat(
                                    videoTitle = videoTitle,
                                    platform = videoPlatform,
                                    videoDescription = videoDescription,
                                    transcript = videoTranscript,
                                    customPrompt = customVideoQuery,
                                    activeBrand = brandProfile
                                )
                                videoReportResult = report
                                isAnalyzingVideo = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan, contentColor = PrimaryDark),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (isAnalyzingVideo) {
                            CircularProgressIndicator(color = PrimaryDark, strokeWidth = 2.dp, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Analyzing with gemini-3.1-pro-preview...", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Run Video Forensics Analysis (Gemini Pro)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Video Result Card
                if (videoReportResult != null) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(SecondaryDark)
                                .border(1.dp, VividLime.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                                .padding(14.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("FORENSIC VIDEO ASSESSMENT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = VividLime)
                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .clickable {
                                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                clipboard.setPrimaryClip(ClipData.newPlainText("Video Report", videoReportResult))
                                                Toast.makeText(context, "Report copied!", Toast.LENGTH_SHORT).show()
                                            }
                                            .padding(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = null, tint = TextMuted, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Copy", fontSize = 10.sp, color = TextMuted)
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = videoReportResult ?: "",
                                    fontSize = 11.sp,
                                    color = TextWhite,
                                    lineHeight = 16.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PresetVideoChip(label: String, platform: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceCard)
            .border(1.dp, SurfaceBorderLight, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Column {
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Text(platform, fontSize = 9.sp, color = ElectricCyan)
        }
    }
}

@Composable
private fun ChatMessageBubble(
    msg: IrisChatMessage,
    onCopy: () -> Unit
) {
    val isUser = msg.sender == "user"

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
            modifier = Modifier.padding(bottom = 3.dp)
        ) {
            if (!isUser) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = VividLime, modifier = Modifier.size(12.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("IRIS Security Copilot", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = VividLime)
            } else {
                Text("Security Analyst", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(msg.timestamp, fontSize = 9.sp, color = TextSubtle)
        }

        Box(
            modifier = Modifier
                .widthIn(max = 340.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isUser) VividLimeDark else SurfaceCard)
                .border(1.dp, if (isUser) VividLime.copy(alpha = 0.5f) else SurfaceBorder, RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Column {
                Text(
                    text = msg.content,
                    fontSize = 12.sp,
                    color = TextWhite,
                    lineHeight = 17.sp,
                    fontFamily = if (msg.content.contains("════") || msg.content.contains("RECIPIENT:")) FontFamily.Monospace else FontFamily.Default
                )

                if (!isUser && msg.content.length > 100) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { onCopy() }
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = TextMuted, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copy to clipboard", fontSize = 10.sp, color = TextMuted)
                    }
                }
            }
        }
    }
}
