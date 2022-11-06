# White Rabbit

Uma aplicação para o envio e recebimento remoto de arquivos e pastas.

> **Autores:**
> 
> César Billalta Yamasato, número USP 1254299;
> 
> Guilherme de Abreu Barreto, número USP 12543033.

## Instruções para compilação

Para baixar e compilar este programa, faça uso dos seguintes comandos:

```shell
git clone https://github.com/CesarYamasato/Ep-Redes.git
cd Ep-Redes
javac **.java
```

## Modo de usar

Instruções de uso são exibidas chamando a aplicação sem o uso de argumentos:

```shell
> java path.to.Peer
```

> Sendo `path.to.Peer` o caminho para o arquivo `Peer.class`, descontada a extensão do arquivo. Fosse esse comando utilizado diretamente na pasta `Ep-Redes`, esse caminho seria `main.Peer`.

É possível participar em uma conexão entre dois peers iniciando-a ‒ com a flag `-c`, para "client" ‒ ou aguardando-a ‒ com a flag `-w`, para "wait". Na eventualidade de o endereço do outro peer ser desconhecida, a flag `-s` pode se combinada para buscar (`-cs`),  ou disponibilizar (`-ws`), um endereço de IP para conexão. Ao conectarem-se os dois computadores podem trocar arquivos e navegar em pastas conforme fornecem alguma das instruções fornecidas.

## Protocolo de comunicação

O cliente de White Rabbit pode enviar as seguintes requisições:

**0. Conectar-se ao servidor de destino**

O cliente então cria seu respectivo servidor local e envia seu IP e porta ao servidor de destino para iniciar uma conexão de duas mãos, conforme descreve o método `main` de `Peer.java`.

**1. Listar o conteúdo na atual pasta**

O servidor então responde (pelo método `sendListDirectory()`) enviando:

- A quantidade de arquivos e pastas

- Uma sequência descrevendo para cada pasta ou arquivo
  
  - O tamanho da `String`, contendo  seu nome;
  
  - Um `boolean` indicando se este é um arquivo ou pasta.

Esta sequência é percorrida pelo cliente no método `receiveListDirectory()` e descrita na tela.

**2. Envio de arquivo ou pasta**

O cliente envia o índice do arquivo ou pasta na lista recebida a ser enviada o servidor então responde enviando uma...

**3. Navegar a outra pasta**

...

**4. Encerrar a conexão**

Fecham-se os `Sockets` e `Streams`, a opção 4 é enviada ao servidor. Este exibe uma mensagem na tela a seu usuário indicando que a conexão com este Peer foi encerrada.

## Testes feitos

### Conexão

Relatar em rede local e rede remota, em conexão inicial e subsequente.

### Download de arquivos

Relatar para arquivos individuais e pastas inteiras

### Navegação de diretórios

Relatar navegação a diretórios filhos e pais. 

## Arquiteturas utilizadas

Para o desenvolvimento desta aplicação e subsequentes testes destas, as seguintes máquinas foram utilizadas:

### Máquina 1

```shell
> java --version
openjdk 17.0.4 2022-07-19
OpenJDK Runtime Environment (build 17.0.4+8-Ubuntu-120.04)
OpenJDK 64-Bit Server VM (build 17.0.4+8-Ubuntu-120.04, mixed mode, sharing)
> inxi -b
System:    Host: cesar-Inspiron-3501 Kernel: 5.14.0-1052-oem x86_64 bits: 64 Desktop: Xfce 4.16.0 
           Distro: Linux Mint 20.3 Una 
Machine:   Type: Laptop System: Dell product: Inspiron 3501 v: N/A serial: <superuser/root required> 
           Mobo: Dell model: 06PFMP v: A00 serial: <superuser/root required> UEFI: Dell v: 1.15.0 
           date: 05/17/2022 
Battery:   ID-1: BAT0 charge: 27.2 Wh condition: 27.2/42.0 Wh (65%) 
CPU:       Quad Core: 11th Gen Intel Core i7-1165G7 type: MT M
```

### Máquina 2

```shell
> java --version
openjdk 19.0.1 2022-10-18
OpenJDK Runtime Environment (build 19.0.1+10)
OpenJDK 64-Bit Server VM (build 19.0.1+10, mixed mode)
> inxi -b
System:
  Host: manjaro Kernel: 5.15.76-1-MANJARO arch: x86_64 bits: 64 Desktop: GNOME
    v: 42.5 Distro: Manjaro Linux
Machine:
  Type: Portable System: Dell product: Inspiron 5548 v: A10
    serial: <superuser required>
  Mobo: Dell model: 0YDTG3 v: A02 serial: <superuser required>
    UEFI-[Legacy]: Dell v: A10 date: 05/28/2019
Battery:
  ID-1: BAT1 charge: 7.4 Wh (26.4%) condition: 28.0/42.2 Wh (66.3%)
    volts: 10.2 min: 11.1
CPU:
  Info: dual core Intel Core i7-5500U [MT MCP] speed (MHz): avg: 2293
    min/max: 500/3000
Graphics:
  Device-1: Intel HD Graphics 5500 driver: i915 v: kernel
  Device-2: AMD Topaz XT [Radeon R7 M260/M265 / M340/M360 M440/M445 530/535
    620/625 Mobile] driver: amdgpu v: kernel
  Device-3: Sunplus Innovation Integrated_Webcam_HD type: USB
    driver: uvcvideo
  Display: x11 server: X.org v: 1.21.1.4 with: Xwayland v: 22.1.4 driver: X:
    loaded: amdgpu,modesetting unloaded: radeon dri: iris,radeonsi gpu: i915
    resolution: 1920x1080~60Hz
  API: OpenGL v: 4.6 Mesa 22.2.1 renderer: Mesa Intel HD Graphics 5500 (BDW
    GT2)
Network:
  Device-1: Realtek RTL810xE PCI Express Fast Ethernet driver: r8169
  Device-2: Intel Wireless 7265 driver: iwlwifi
  Device-3: Intel Bluetooth wireless interface type: USB driver: btusb
Drives:
  Local Storage: total: 447.13 GiB used: 381.55 GiB (85.3%)
Info:
  Processes: 338 Uptime: 6h 54m Memory: 15.55 GiB used: 7.16 GiB (46.1%)
  Shell: fish inxi: 3.3.23
```