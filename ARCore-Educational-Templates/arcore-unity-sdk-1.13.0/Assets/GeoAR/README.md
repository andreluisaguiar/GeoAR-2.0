# GeoAR — Guia de Configuração e Uso

Aplicativo educacional de Geometria em AR. O aluno detecta planos no mundo real, posiciona formas 3D e responde perguntas sobre elas.

## Visão Geral
- Engine: Unity (C#)
- AR: AR Foundation (ARCore/Android, ARKit/iOS)
- Detecção: Planes horizontais
- Fluxo: tocar para colocar forma → tocar na forma → responder quiz → avançar nível ao acertar

## Estrutura
```
Assets/GeoAR/
  Scripts/            // GameManager, ARPlacementController, QuizManager, GeometryQuestion, DefaultQuestions
  Prefabs/            // Prefabs das formas geométricas
  UI/                 // Canvas, botões, textos
  Data/               // (Opcional) ScriptableObjects de perguntas
  Materials/          // Materiais e texturas
```

## Cena Automática (Bootstrap)
- O arquivo [Assets/GeoAR/Scripts/BootstrapARScene.cs](Assets/GeoAR/Scripts/BootstrapARScene.cs) monta automaticamente a cena ao iniciar (ARSession, ARSessionOrigin, AR Camera, ARPlaneManager, ARRaycastManager, UI e integra `GameManager` e `QuizManager`).
- Isso permite rodar sem configurar manualmente a cena.

## Prefabs pelo Editor
- Use o menu GeoAR → "Gerar Prefabs Básicos" para criar prefabs em [Assets/GeoAR/Prefabs](Assets/GeoAR/Prefabs).
- Arquivo: [Assets/GeoAR/Editor/PrefabGenerator.cs](Assets/GeoAR/Editor/PrefabGenerator.cs).
- Gera: Cubo, Esfera, Cilindro, Cone, Pirâmide Quadrangular, Prisma Triangular, Toro e Dodecaedro (placeholder com esfera).
 - Também gera duplicatas em [Assets/GeoAR/Resources/Prefabs](Assets/GeoAR/Resources/Prefabs) para carregamento automático em runtime.

## Pacotes Necessários
Instale via Package Manager:
- AR Foundation (com.unity.xr.arfoundation)
- ARCore XR Plugin (com.unity.xr.arcore) [Android]
- ARKit XR Plugin (com.unity.xr.arkit) [iOS]

## Scripts do Projeto
- `GeometryQuestion`: dados de pergunta (forma, pergunta, 3 opções, índice correto)
- `DefaultQuestions`: lista com 8 perguntas (Cubo, Esfera, Cilindro, Cone, Pirâmide Quadrangular, Prisma Triangular, Dodecaedro, Toro)
- `GameManager`: fluxo do jogo, níveis, pontuação; usa `ARPlacementController` e `QuizManager`
- `ARPlacementController`: raycast em plano, instância do prefab, toque na forma para abrir quiz
- `QuizManager`: UI de pergunta, 3 opções, feedback, fim de jogo

### Formas em Runtime
- `RuntimeShapeLibrary`: gera Cone, Pirâmide Quadrangular, Prisma Triangular e Toro por código.
- Dodecaedro está com placeholder (Esfera) até importarmos um modelo dedicado.
 - `GameManager` tenta auto-mapear prefabs em `Start()` usando `Resources.Load("Prefabs/<Nome>")` se `shapePrefabs` estiver vazio ou incompleto.

## Configuração da Cena (Passo a Passo)
1. Crie uma cena vazia.
2. Adicione `AR Session` (GameObject com componente `ARSession`).
3. Adicione `AR Session Origin` ou `XROrigin` (conforme versão):
   - Inclua uma `AR Camera` como filho.
   - Adicione `ARRaycastManager` no mesmo objeto do Origin.
   - Adicione `ARPlaneManager` (Detection Mode: Horizontal).
4. Adicione `ARPlacementController` no mesmo objeto que contém `ARRaycastManager`.
   - Referências no Inspector:
     - `raycastManager` → `ARRaycastManager`
     - `planeManager` → `ARPlaneManager`
     - `arCamera` → sua AR Camera
5. Crie um `Canvas` (Screen Space - Overlay é suficiente) e adicione:
   - `Panel` com `CanvasGroup` (ligar em `QuizManager.panel`)
   - `Text` para pergunta (ligar em `QuizManager.questionText`)
   - 3 `Button` (ligar em `QuizManager.optionA`, `optionB`, `optionC`)
   - `Text` para feedback (ligar em `QuizManager.feedbackText`)
6. Adicione `QuizManager` em um GameObject e ligue todas as referências acima.
7. Adicione `GameManager` em um GameObject e conecte:
   - `quizManager` → objeto com `QuizManager`
   - `placementController` → objeto com `ARPlacementController`
   - `shapePrefabs` → mapeie cada `shapeName` para seu `Prefab` (ver abaixo).

## Prefabs das Formas
Mapeie exatamente estes nomes em `GameManager.shapePrefabs`:
- "Cubo" → prefab de Cubo
- "Esfera" → prefab de Esfera
- "Cilindro" → prefab de Cilindro
- "Cone" → prefab de Cone
- "Pirâmide Quadrangular" → prefab dessa pirâmide
- "Prisma Triangular" → prefab de prisma triangular
- "Dodecaedro" → prefab de dodecaedro
- "Toro" → prefab de toro (rosquinha)

Dica: use primitivas do Unity para Cubo, Esfera e Cilindro. Para Cone, Pirâmide, Prisma, Dodecaedro e Toro, importe modelos `.fbx`/`.obj` simples e crie os prefabs em `Assets/GeoAR/Prefabs`.
Ou execute o menu "Gerar Prefabs Básicos" para criar automaticamente os prefabs necessários.

## Checklist de Setup
- [ ] Pacotes AR Foundation/ARCore/ARKit instalados
- [ ] Cena possui `ARSession`, `ARSessionOrigin/XROrigin`, `AR Camera`
- [ ] `ARRaycastManager` e `ARPlaneManager` configurados (Horizontal)
- [ ] `ARPlacementController` com referências ligadas
- [ ] Canvas/UI montados e ligados ao `QuizManager`
- [ ] `GameManager` com `quizManager` e `placementController` ligados
- [ ] `shapePrefabs` preenchidos com os 8 prefabs

## Execução e Teste
1. Abra a cena no Unity.
2. Faça Build & Run para um dispositivo (Android/iOS).
3. Aponte a câmera para uma superfície plana e iluminada.
4. Toque na tela para colocar a forma.
5. Toque na forma colocada para abrir o quiz.
6. Responda; ao acertar, avança para a próxima forma.

## Build
### Android
- Player Settings → ARCore habilitado.
- Permissões de câmera e Min SDK compatível.
- `Build Settings` → Android, `Build & Run`.

### iOS
- Player Settings → ARKit habilitado.
- Permissão de uso da câmera no `Info.plist`.
- Gere Xcode project e rode em dispositivo físico.

## Solução de Problemas
- Nada aparece: verifique luz, textura/cores, se há planos horizontais suficientes.
- Não coloca objeto: confira `ARRaycastManager` e `ARPlaneManager` ativos; toque inicial deve ser em um plano detectado.
- Toque não abre quiz: garanta `Collider` no prefab; o controlador adiciona `BoxCollider` se não houver.
- Nomes das formas: precisam coincidir com `shapeName` dos dados (ver `DefaultQuestions`).

---
Para cena exemplo pronta ou importação de modelos básicos (cone, toro, pirâmide, etc.), solicite e eu adiciono nesta pasta com o mapeamento no `GameManager`.
