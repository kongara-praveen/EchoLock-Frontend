# UI Styling Guide for EchoLock App

## Overview
This guide provides the pattern for updating all screens with consistent styling, bright text, proper alignment, and animations.

## Theme Constants
All screens should use `AppColors` from `com.example.echolock.ui.theme.AppTheme`:

```kotlin
// Colors
// Use: import com.example.echolock.ui.theme.AppColors
AppColors.Background        // Screen background (0xFFF8FAFC)
AppColors.Surface          // Card/container background (White)
AppColors.TextPrimary      // Main text (0xFF0A2E45) - BRIGHT
AppColors.TextSecondary    // Secondary text (0xFF6B7E80)
AppColors.TextTertiary     // Placeholder text (0xFF8A9AA0)
AppColors.PrimaryDark      // Primary color (0xFF005F73)
AppColors.BorderLight      // Borders (0xFFD0DBDF)
```

## Standard Screen Pattern

### 1. Background & Container
```kotlin
Column(
    modifier = Modifier
        .fillMaxSize()
        .background(AppColors.Background)
        .alpha(alpha)  // For entrance animation
        .padding(horizontal = 20.dp, vertical = 16.dp)
) {
    // Content
}
```

### 2. Text Input Fields
```kotlin
OutlinedTextField(
    value = text,
    onValueChange = { text = it },
    textStyle = TextStyle(
        fontSize = 16.sp,
        color = AppColors.TextPrimary,  // BRIGHT TEXT
        fontWeight = FontWeight.Normal
    ),
    colors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = AppColors.TextPrimary,
        unfocusedTextColor = AppColors.TextPrimary,
        focusedBorderColor = AppColors.PrimaryDark,
        unfocusedBorderColor = AppColors.BorderLight,
        cursorColor = AppColors.PrimaryDark,
        focusedPlaceholderColor = AppColors.TextTertiary,
        unfocusedPlaceholderColor = AppColors.TextTertiary
    ),
    shape = RoundedCornerShape(14.dp)
)
```

### 3. Buttons
```kotlin
Button(
    enabled = buttonEnabled,
    modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .alpha(buttonAlpha),  // Animation
    shape = RoundedCornerShape(14.dp),
    colors = ButtonDefaults.buttonColors(
        containerColor = AppColors.PrimaryDark,
        disabledContainerColor = AppColors.BorderLight
    ),
    elevation = ButtonDefaults.buttonElevation(
        defaultElevation = 4.dp,
        pressedElevation = 2.dp
    )
) {
    Text(
        text = "Button Text",
        color = Color.White,
        fontSize = 17.sp,
        fontWeight = FontWeight.SemiBold
    )
}
```

### 4. Screen Entrance Animation
```kotlin
val alpha by animateFloatAsState(
    targetValue = 1f,
    animationSpec = tween(durationMillis = 300),
    label = "screen_alpha"
)

// Apply to Column modifier:
.alpha(alpha)
```

### 5. Button Press Animation
```kotlin
val buttonEnabled by remember { derivedStateOf { /* condition */ } }
val buttonAlpha by animateFloatAsState(
    targetValue = if (buttonEnabled) 1f else 0.6f,
    animationSpec = tween(durationMillis = 200),
    label = "button_alpha"
)
```

### 6. Card/Tile Press Animation
```kotlin
var isPressed by remember { mutableStateOf(false) }
val scale by animateFloatAsState(
    targetValue = if (isPressed) 0.98f else 1f,
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    ),
    label = "tile_scale"
)

Card(
    modifier = Modifier.scale(scale)
        .clickable { isPressed = true; onClick() }
) {
    // Content
}
```

## Text Sizes & Weights
- **Headings**: 22-28sp, FontWeight.Bold, AppColors.TextPrimary
- **Body**: 15-16sp, FontWeight.Normal, AppColors.TextPrimary
- **Secondary**: 13-14sp, FontWeight.Normal, AppColors.TextSecondary
- **Placeholders**: 15-16sp, AppColors.TextTertiary

## Spacing
- Small: 8.dp
- Medium: 16.dp
- Large: 24.dp
- Extra Large: 32.dp

## Checklist for Each Screen
- [ ] Background: AppColors.Background
- [ ] Text colors: AppColors.TextPrimary (bright)
- [ ] Input fields: Bright text, proper colors
- [ ] Buttons: Proper elevation, animations
- [ ] Screen entrance animation
- [ ] Proper alignment and padding
- [ ] Consistent spacing

