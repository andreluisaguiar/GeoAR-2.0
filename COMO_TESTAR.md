# 🚀 Como Testar o GeoAR

## ⚡ Teste Rápido (Android Studio)

### Passo 1: Abrir o Projeto

```bash
# Abra o Android Studio e selecione:
# File > Open > /home/andre-aguiar/Documentos/GeoAR
```

### Passo 2: Sincronizar Gradle

O Android Studio irá automaticamente:
- ✅ Baixar o Gradle wrapper
- ✅ Sincronizar dependências
- ✅ Resolver dependências do ARCore

Aguarde a barra de progresso no canto inferior do Android Studio.

### Passo 3: Conectar Dispositivo

**Opção A - Dispositivo Físico (Recomendado para AR):**
1. Conecte seu dispositivo via USB
2. Habilite **Depuração USB**:
   - Configurações > Sobre o telefone
   - Toque 7x em "Número da versão"
   - Volte > Opções do desenvolvedor
   - Ative "Depuração USB"
3. Aceite a autorização no dispositivo

**Opção B - Emulador (Limitado, sem AR):**
- Não recomendado pois ARCore não funciona bem em emuladores

### Passo 4: Build e Run

1. Clique no botão **▶️ Run** (Shift + F10)
2. Selecione seu dispositivo na lista
3. Aguarde a instalação
4. O app abrirá automaticamente

### Passo 5: Usar o App

1. **Na tela inicial:**
   - Veja o status do ARCore
   - Selecione um módulo (Básico, Intermediário)
   - Escolha uma forma geométrica

2. **Na tela AR:**
   - Apontar câmera para superfície plana
   - Aguardar detecção de plano
   - Toque na tela para posicionar a forma
   - Use botões para interagir

## 📊 Status Atual

✅ **Código Fonte**: 100% Completo
- 18 arquivos Kotlin
- Arquitetura MVVM
- ARCore integrado
- OpenGL ES rendering
- Jetpack Compose UI

✅ **Dependências**: Configuradas
- ARCore SDK
- Jetpack Compose  
- OpenGL ES
- Material Design

⚠️ **Teste**: Precisa Android Studio
- Gradle wrapper pronto
- Apenas necessita sincronização

## 🐛 Troubleshooting

### "Gradle sync failed"

```bash
cd /home/andre-aguiar/Documentos/GeoAR
rm -rf .gradle
rm -rf build
# Depois no Android Studio: File > Invalidate Caches / Restart
```

### "ARCore não está disponível"

- Instale ARCore do Play Store
- Verifique compatibilidade do dispositivo
- Reinicie o dispositivo

### "Could not find com.google.ar:core"

```bash
# No Android Studio terminal:
./gradlew --refresh-dependencies
```

### Build Succeeds mas App Crasha

- Verifique se o dispositivo tem ARCore
- Verifique permissões da câmera
- Veja logs: `adb logcat | grep GeoAR`

## 📱 Requisitos do Dispositivo

- Android 7.0+ (API 24+)
- ARCore instalado
- Câmera traseira
- Sensores de movimento
- Boa iluminação

## ✨ Verificar se Funcionou

Depois de rodar o app, você deve ver:

1. **MainActivity**: 
   - Menu com formas geométricas
   - Botões de seleção

2. **AR Activity**:
   - Câmera ativada
   - Detecção de superfície
   - Possibilidade de posicionar objetos

## 📞 Próximos Passos

Depois que o app estiver rodando:

1. Teste cada forma geométrica
2. Verifique o conteúdo educativo
3. Teste interações (toque, arrastar)
4. Verifique salvamento de progresso

## 🎯 Comandos Úteis

```bash
# Ver logs em tempo real
adb logcat | grep GeoAR

# Listar devices conectados
adb devices

# Instalar APK direto
./gradlew assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk

# Limpar e rebuild
./gradlew clean assembleDebug
```

---

**🎉 Pronto para testar!** 

Abra o Android Studio e divirta-se! 🚀

