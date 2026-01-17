# GeoAR - Aplicativo Educacional de Geometria em AR

Aplicativo educacional para ensino de geometria usando Realidade Aumentada (AR). Desenvolvido com Unity 3D e AR Foundation.

## 🎯 Objetivo

O aluno encontra formas geométricas no mundo real através da câmera do dispositivo e responde perguntas educacionais sobre elas. O jogo possui 8 formas com perguntas progressivas.

## 📱 Formas Geométricas Incluídas

1. **Cubo** - "Quantas faces tem um cubo?" (6)
2. **Esfera** - "A esfera possui arestas?" (Não)
3. **Cilindro** - "Qual é a forma da base de um cilindro?" (Círculo)
4. **Cone** - "Quantos vértices tem um cone?" (1)
5. **Pirâmide Quadrangular** - "Quantas faces triangulares?" (4)
6. **Prisma Triangular** - "Quantas arestas?" (9)
7. **Dodecaedro** - "Quantas faces?" (12)
8. **Toro** - "O toro é um poliedro?" (Não)

## 🛠 Tecnologias

- **Engine**: Unity 3D (C#)
- **AR Framework**: AR Foundation
- **Plataforma**: Android (ARCore) / iOS (ARKit)
- **Detecção**: Plane Detection (Horizontal)

## 📂 Estrutura do Projeto

```
GeoAR/
├── ARCore-Educational-Templates/
│   └── arcore-unity-sdk-1.13.0/
│       └── Assets/GeoAR/
│           ├── Scripts/          # GameManager, ARPlacementController, etc
│           ├── Prefabs/          # Modelos das formas
│           ├── Resources/        # Prefabs carregáveis em runtime
│           ├── Editor/           # PrefabGenerator (menu Unity)
│           ├── UI/               # Canvas e elementos de UI
│           ├── Materials/        # Texturas e materiais
│           ├── README.md         # Documentação do projeto
│           └── BUILD_APK_GUIDE.md # Guia detalhado para build
├── app/                          # Módulo Android (gradle)
├── build.gradle.kts              # Configuração Gradle
└── README.md                      # Este arquivo
```

## 🚀 Quickstart

### Requisitos
- Unity 2022 LTS ou superior
- Android SDK/NDK
- Dispositivo Android com API 24+ e suporte a ARCore

### Setup Rápido

1. **Clone o repositório**
   ```bash
   git clone https://github.com/seu-usuario/GeoAR.git
   cd GeoAR/ARCore-Educational-Templates/arcore-unity-sdk-1.13.0
   ```

2. **Abra no Unity Hub**
   - Adicione o projeto: `/caminho/para/GeoAR/ARCore-Educational-Templates/arcore-unity-sdk-1.13.0`
   - Selecione versão Unity 2022+ com Android Build Support

3. **Gere os prefabs**
   - No Unity Editor: **GeoAR → Gerar Prefabs Básicos**

4. **Faça o build**
   - Siga o guia: [BUILD_APK_GUIDE.md](ARCore-Educational-Templates/arcore-unity-sdk-1.13.0/Assets/GeoAR/BUILD_APK_GUIDE.md)

## 📖 Documentação Detalhada

- **[BUILD_APK_GUIDE.md](ARCore-Educational-Templates/arcore-unity-sdk-1.13.0/Assets/GeoAR/BUILD_APK_GUIDE.md)** - Guia completo para build no Windows e Linux
- **[Assets/GeoAR/README.md](ARCore-Educational-Templates/arcore-unity-sdk-1.13.0/Assets/GeoAR/README.md)** - Documentação técnica do projeto

## 🎮 Como Usar o App

1. Abra o aplicativo no dispositivo
2. Aponte a câmera para uma **superfície plana e bem iluminada**
3. Toque na tela para **colocar a forma** na detecção de plano
4. Toque na **forma geométrica** para abrir o quiz
5. Responda a pergunta corretamente
6. Avance para a **próxima forma** ao acertar
7. Veja sua **pontuação final** ao terminar as 8 formas

## 🔧 Principais Scripts

- **GameManager.cs** - Fluxo do jogo, pontuação, níveis
- **ARPlacementController.cs** - Detecção de plano e colocação de objetos
- **QuizManager.cs** - UI de perguntas e feedback
- **RuntimeShapeLibrary.cs** - Geração de formas 3D em runtime
- **DefaultQuestions.cs** - Base de dados das 8 perguntas

## 🌟 Recursos Extras

- ✅ Bootstrap automático de cena (sem configuração manual)
- ✅ Auto-mapeamento de prefabs via Resources
- ✅ Gerador de prefabs no Editor (menu GeoAR)
- ✅ Fallback de formas em runtime (sem prefabs)
- ✅ Suporte a Android/iOS

## 🐛 Troubleshooting

### "Android SDK not found"
Veja a seção "Troubleshooting" em [BUILD_APK_GUIDE.md](ARCore-Educational-Templates/arcore-unity-sdk-1.13.0/Assets/GeoAR/BUILD_APK_GUIDE.md)

### "ARCore not working"
- Verifique se o dispositivo suporta ARCore: https://developers.google.com/ar/devices
- Instale "Google Play Services for AR"

### Mais ajuda
Consulte [Assets/GeoAR/README.md](ARCore-Educational-Templates/arcore-unity-sdk-1.13.0/Assets/GeoAR/README.md) para detalhes técnicos.

## 📄 Licença

Este projeto usa AR Foundation do Unity e é licenciado sob MIT License.

## 👨‍💻 Desenvolvimento

Desenvolvido por **André Aguiar** como projeto educacional de Realidade Aumentada.

---

**Versão**: 1.0  
**Data**: Janeiro 2026  
**Status**: ✅ Pronto para produção
