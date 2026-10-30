import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReservasComponent } from './components/reservas/reservas.component';
import { BancoComponent } from './components/banco/banco.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, ReservasComponent, BancoComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  activeTab: 'reservas' | 'banco' = 'reservas';

  setTab(tab: 'reservas' | 'banco'): void {
    this.activeTab = tab;
  }
}
