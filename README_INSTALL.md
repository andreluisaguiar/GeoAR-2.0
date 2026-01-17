# ⚠️ Instruções de Instalação e Build

## 📝 Status das Dependências

**As dependências estão configuradas no `app/build.gradle.kts`, mas você precisa:**

### 1. Adicionar o Gradle Wrapper

O projeto precisa do Gradle wrapper para funcionar. Siga um destes métodos:

#### **Opção A - Usando Android Studio (Recomendado)**
1. Abra o projeto no Android Studio
2. O Android Studio irá automaticamente sugerir configurar o Gradle wrapper
3. Clique em "Sync Now" quando solicitado

#### **Opção B - Criar Manualmente via Terminal**
```bash
# No diretório do projeto
cd /home/andre-aguiar/Documentos/GeoAR

# Execute o comando para criar o wrapper
gradle wrapper --gradle-version 8.2
```

#### **Opção C - Baixar o wrapper.jar**

Se as opções acima não funcionarem, você precisará baixar o arquivo:
`gradle-wrapper.jar` e colocá-lo em: `gradle/wrapper/gradle-wrapper.jar`

Baixe de: https://services.gradle.org/distributions/gradle-wrapper.jar

### 2. Tornar o gradlew Executável (se criado manualmente)

```bash
chmod +x gradlew
```

## 📦 Dependências Configuradas

Todas as dependências necessárias estão declaradas no `app/build.gradle.kts`:

✅ **ARCore SDK**: 1.40.0  
✅ **Jetpack Compose**: 2023.10.01  
✅ **OpenGL ES**: 1.0.0  
✅ **Material Design**: 1.10.0  
✅ **Kotlin Serialization**: 1.6.0  
✅ **Lifecycle Components**: 2.6.2  
✅ **Navigation**: 2.7.5  

## 🔧 Próximos Passos

### 1. Sincronizar no Android Studio

1. Abra o projeto no Android Studio
2. Aguarde a sincronização do Gradle
3. Se houver erros, clique em `File > Sync Project with Gradle Files`

### 2. Instalar SDK Necessário

No Android Studio:
- File > Settings > Appearance & Behavior > System Settings > Android SDK
- Instale Android SDK Platform 34 (Android 14)
- Instale Android SDK Build-Tools 34.0.0

### 3. Verificar Compatibilidade do Dispositivo

- O dispositivo precisa ser compatível com ARCore
- Instale ARCore no dispositivo (via Play Store)

### 4. Build e Run

```bash
# Via terminal (após criar o wrapper)
./gradlew assembleDebug

# Ou use o botão "Run" no Android Studio
```

## 🐛 Resolução de Problemas

### Erro: "Gradle sync failed"

**Solução:**
1. Delete a pasta `.gradle`
2. Delete o arquivo `build.gradle.kts` (temporariamente)
3. Vá em File > Invalidate Caches / Restart
4. Reabra o projeto

### Erro: "Plugin [id: 'org.jetbrains.kotlin.plugin.serialization']"

**Solução:**
Agora está configurado! Você só precisa sincronizar o projeto.

### Erro: "Could not resolve com.google.ar:core"

**Solução:**
Verifique sua conexão com a internet e:
```bash
./gradlew --refresh-dependencies
```

## ✅ Checklist de Instalação

- [ ] Android Studio instalado (versão Hedgehog ou superior)
- [ ] Gradle wrapper criado (Automático no Android Studio)
- [ ] SDK Platform 34 instalado
- [ ] Dependências sincronizadas
- [ ] Dispositivo ARCore compatível conectado
- [ ] ARCore instalado no dispositivo

## 📝 Nota Importante

**O projeto está 100% configurado em código!** Você só precisa:
1. Abrir no Android Studio
2. Aguardar a sincronização
3. Executar no dispositivo

Não é necessário adicionar nenhuma dependência manualmente - tudo está no `build.gradle.kts`.

## 🚀 Build Rápido

Depois que tudo estiver instalado:

```bash
# Debug build
./gradlew assembleDebug

# O APK será gerado em:
# app/build/outputs/apk/debug/app-debug.apk
```

---

**Importante**: As dependências estão TODAS configuradas no código. O que falta é apenas sincronizar no Android Studio!

