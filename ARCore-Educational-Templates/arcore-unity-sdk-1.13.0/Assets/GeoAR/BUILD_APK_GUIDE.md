# Guia Completo: Build APK do GeoAR com Unity Hub

Este documento explica passo a passo como criar o APK do aplicativo GeoAR usando Unity Hub, tanto no **Windows** quanto no **Linux**.

---

## Índice
1. [Instalação do Unity Hub](#instalação-do-unity-hub)
2. [Setup Android (SDK/NDK)](#setup-android-sdkndk)
3. [Configuração no Unity Editor](#configuração-no-unity-editor)
4. [Build do APK](#build-do-apk)
5. [Instalação no Dispositivo](#instalação-no-dispositivo)
6. [Troubleshooting](#troubleshooting)

---

## Instalação do Unity Hub

### Windows

1. **Baixe o Unity Hub**
   - Acesse: https://unity.com/download
   - Clique em "Download Unity Hub"
   - Execute o instalador (`UnityHubSetup.exe`)

2. **Instale o Unity Editor**
   - Abra Unity Hub
   - Clique em "Installs" → "+ Add"
   - Selecione uma versão recente (2022 LTS ou 2023)
   - Na instalação, marque **Android Build Support** ✓

3. **Linked Projects**
   - Clique em "Projects" → "+ Add"
   - Navegue até: `/caminho/para/GeoAR/ARCore-Educational-Templates/arcore-unity-sdk-1.13.0`
   - Clique em "Open"
   - Unity Hub detectará a versão necessária

### Linux

1. **Baixe o Unity Hub**
   ```bash
   # Via snap (recomendado)
   sudo snap install unityhub --classic

   # Ou baixe manualmente em: https://unity.com/download
   # Execute o arquivo .AppImage
   chmod +x UnityHub.AppImage
   ./UnityHub.AppImage
   ```

2. **Instale o Unity Editor**
   - Abra Unity Hub
   - Clique em "Installs" → "+ Add"
   - Selecione versão recente (2022 LTS ou 2023)
   - Na instalação, marque **Android Build Support** ✓
   - Aguarde conclusão (pode levar 30-45 min)

3. **Linked Projects**
   - Clique em "Projects" → "+ Add"
   - Navegue até: `/caminho/para/GeoAR/ARCore-Educational-Templates/arcore-unity-sdk-1.13.0`
   - Clique em "Open"

---

## Setup Android (SDK/NDK)

Antes de fazer build, você precisa do Android SDK e Android NDK.

### Windows

#### Opção 1: Android Studio (Recomendado)

1. **Instale Android Studio**
   - Baixe em: https://developer.android.com/studio
   - Execute o instalador
   - Complete o setup wizard (instala Android SDK automaticamente)

2. **Localize os caminhos**
   - Android SDK geralmente fica em: `C:\Users\SeuUsuario\AppData\Local\Android\Sdk`
   - Android NDK geralmente fica em: `C:\Users\SeuUsuario\AppData\Local\Android\Sdk\ndk\<versão>`

#### Opção 2: Command Line Tools

1. **Baixe e extraia Command Line Tools**
   - Acesse: https://developer.android.com/studio
   - Role até "Command line tools only"
   - Extraia em: `C:\Android\cmdline-tools`

2. **Configure variáveis de ambiente**
   ```cmd
   # Abra "Variáveis de Ambiente" (Environment Variables)
   ANDROID_SDK_ROOT=C:\Android
   ANDROID_NDK_ROOT=C:\Android\ndk\25.1.8937393
   ```

3. **Instale SDK/NDK via script**
   ```cmd
   cd C:\Android\cmdline-tools\bin
   sdkmanager --sdk_root=C:\Android "platforms;android-30" "build-tools;30.0.3" "ndk;25.1.8937393"
   ```

### Linux

#### Opção 1: Android Studio

```bash
# Baixe Android Studio
wget https://redirector.gstatic.com/android/studio/install/2023.1.1/android-studio-2023.1.1-linux.tar.gz

# Extraia
tar -xzf android-studio-2023.1.1-linux.tar.gz
sudo mv android-studio /opt/

# Execute
/opt/android-studio/bin/studio.sh
```

Android SDK será instalado em: `~/Android/Sdk`

#### Opção 2: Command Line Tools

```bash
# Crie a pasta
mkdir -p ~/Android/cmdline-tools

# Baixe Command Line Tools
wget https://dl.google.com/android/repository/commandlinetools-linux-10406996_latest.zip

# Extraia
unzip commandlinetools-linux-10406996_latest.zip -d ~/Android/cmdline-tools/

# Configure variáveis de ambiente (adicione ao ~/.bashrc ou ~/.zshrc)
export ANDROID_SDK_ROOT=$HOME/Android/Sdk
export ANDROID_NDK_ROOT=$HOME/Android/Sdk/ndk/25.1.8937393
export PATH=$PATH:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin

# Recarregue shell
source ~/.bashrc  # ou ~/.zshrc
```

#### Instale SDK/NDK

```bash
cd ~/Android/Sdk/cmdline-tools/latest/bin

# Instale componentes necessários
./sdkmanager --sdk_root=$ANDROID_SDK_ROOT \
    "platforms;android-30" \
    "build-tools;30.0.3" \
    "ndk;25.1.8937393"
```

---

## Configuração no Unity Editor

### 1. Abra o Projeto

1. No Unity Hub, clique no projeto GeoAR
2. Aguarde o Editor carregar (primeira vez leva mais tempo)

### 2. Configure Android Build Support

1. **Abra Build Settings**
   - File → Build Settings (ou Ctrl+Shift+B)

2. **Selecione Android**
   - Na lista "Scenes In Build", deixe vazio (o bootstrap cria a cena)
   - Clique em "Android" → "Switch Platform"
   - Aguarde a compilação (5-10 min)

3. **Configure Player Settings**
   - Clique em "Player Settings"
   - Na aba "Resolution and Presentation":
     - Orientation: Portrait
     - Status Bar Hidden: ✓

4. **Configure XR (AR Foundation)**
   - Em Player Settings → XR Plug-in Management:
     - Marque **ARCore** ✓
     - Leave ARCore Optional: ✓ (permite rodar em não-AR também)

5. **Configure outras opções**
   - Em Player Settings → Other Settings:
     - Minimum API Level: 24 (Android 7.0+)
     - Target API Level: 30+
     - Graphics APIs: OpenGL ES 3

6. **Keystore para assinatura** (importante)
   - Em Player Settings → Publishing Settings:
     - **Opção A: Criar keystore novo**
       - Create New Keystore
       - Alias: `geoar`
       - Password: `sua_senha_segura`
     - **Opção B: Usar keystore existente**
       - Keystore path: `/caminho/para/keystore.jks`
       - Password: (sua senha)
       - Alias: `geoar`
       - Alias Password: (sua senha)

### 3. Configure Câmera

1. **Permissões de Câmera**
   - Player Settings → Other Settings → Camera Usage Description
   - Digite: "GeoAR precisa acessar a câmera para detectar formas geométricas em AR"

---

## Build do APK

### Windows

1. **No Unity Editor**
   - File → Build Settings
   - Clique em "Build" (não "Build and Run")
   - Selecione pasta de destino (ex: `C:\Builds\GeoAR`)
   - Escolha nome do arquivo: `GeoAR.apk`
   - Clique em "Save"

2. **Aguarde a compilação**
   - Tempo: 5-15 minutos (depende do PC)
   - Você verá na console a progressão

3. **APK gerado**
   - Arquivo criado em: `C:\Builds\GeoAR\GeoAR.apk`

### Linux

1. **No Unity Editor**
   - File → Build Settings
   - Clique em "Build" (não "Build and Run")
   - Selecione pasta de destino (ex: `~/Builds/GeoAR`)
   - Escolha nome do arquivo: `GeoAR.apk`
   - Clique em "Save"

2. **Aguarde a compilação**
   - Tempo: 5-15 minutos (depende do processador)
   - Você verá na console a progressão

3. **APK gerado**
   - Arquivo criado em: `~/Builds/GeoAR/GeoAR.apk`

---

## Instalação no Dispositivo

### Pré-requisitos

- Dispositivo Android com API 24+ (Android 7.0+)
- USB Debug habilitado no dispositivo:
  - Configurações → Sobre o telefone → Versão build (clique 7x)
  - Volte → Opções de desenvolvedor → USB Debug ✓

### Windows

1. **Conecte o dispositivo via USB**
   ```cmd
   # Verifique conexão
   adb devices
   # Você deve ver: "device" ao lado do seu telefone
   ```

2. **Instale o APK**
   ```cmd
   cd C:\Builds\GeoAR
   adb install -r GeoAR.apk
   ```

3. **Ou arraste e solte manualmente**
   - Copie `GeoAR.apk` para o dispositivo
   - Abra um File Manager
   - Clique no APK → "Instalar"

### Linux

1. **Conecte o dispositivo via USB**
   ```bash
   adb devices
   # Você deve ver: "device" ao lado do seu telefone
   ```

2. **Instale o APK**
   ```bash
   cd ~/Builds/GeoAR
   adb install -r GeoAR.apk
   ```

3. **Ou arraste e solte manualmente**
   ```bash
   # Via terminal
   adb push GeoAR.apk /sdcard/Download/
   ```

---

## Executar o App

1. **No dispositivo**
   - Procure por "GeoAR" na tela inicial
   - Clique para abrir

2. **Permissões**
   - Ao primeiro acesso, o app pedirá permissão de câmera
   - Clique em "Permitir"

3. **Teste**
   - Aponte para uma superfície plana
   - Toque para colocar a forma
   - Toque na forma para abrir o quiz
   - Responda e avance

---

## Troubleshooting

### "Android SDK not found"

**Windows:**
```cmd
# Abra Edit → Preferences (ou File → Preferences)
# External Tools → Android
# SDK: C:\Users\SeuUsuario\AppData\Local\Android\Sdk
# NDK: C:\Users\SeuUsuario\AppData\Local\Android\Sdk\ndk\25.1.8937393
# JDK: C:\Program Files\Android\Android Studio\jbr
```

**Linux:**
```bash
# Abra Edit → Preferences (ou File → Preferences)
# External Tools → Android
# SDK: $HOME/Android/Sdk
# NDK: $HOME/Android/Sdk/ndk/25.1.8937393
# JDK: /usr/lib/jvm/java-11-openjdk-amd64
```

### "Build failed with error: Keystore password is wrong"

- Verifique a senha do keystore em Player Settings → Publishing Settings
- Ou crie um novo keystore

### "ARCore not working on device"

- Verifique se o dispositivo suporta ARCore
- Acesse: https://developers.google.com/ar/devices
- Instale Google Play Services para AR (Play Store)

### "adb: command not found" (Linux)

```bash
sudo apt install android-tools-adb
```

### "Permission denied on /dev/bus/usb" (Linux)

```bash
# Adicione seu usuário ao grupo plugdev
sudo usermod -aG plugdev $USER
sudo usermod -aG dialout $USER

# Reinicie o terminal ou computador
```

### "Build takes too long"

- Verifique espaço em disco (mínimo 2GB livre)
- Aumente RAM do Unity Editor (Edit → Preferences → Memory)
- Use SSD em vez de HDD

---

## Boas Práticas

1. **Sempre fazer Build (não Build and Run)** primeira vez
2. **Testar em múltiplos dispositivos** (telas/ARCores diferentes)
3. **Manter keystore.jks em local seguro** (necessário para updates)
4. **Versionar o APK**: use `GeoAR-v1.0.apk`, `GeoAR-v1.1.apk`, etc.
5. **Monitorar logs via adb**:
   ```bash
   adb logcat -s GeoAR
   ```

---

## Próximos Passos

- Para publicar na Play Store: https://play.google.com/console
- Para distribuição privada: use Firebase App Distribution
- Para testes: crie versão de Debug com mais logs

---

**Dúvidas?** Consulte a documentação oficial:
- Unity Build: https://docs.unity3d.com/Manual/android-build-process.html
- AR Foundation: https://docs.unity3d.com/Packages/com.unity.xr.arfoundation@5.0/manual/index.html
- ARCore Setup: https://developers.google.com/ar/develop/unity-arf

