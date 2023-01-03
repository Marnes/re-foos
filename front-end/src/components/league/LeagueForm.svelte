<script lang="ts">
    import { LeagueCoefficients, LeagueConfig, LeagueCreation, LeagueType } from '$src/models/league/league';
    import moment from 'moment';
    import { writable, type Writable } from 'svelte/store';
    import { modalStore, RadioGroup, RadioItem, toastStore } from '@skeletonlabs/skeleton';
    import { onMount } from 'svelte';
    import { post } from '$src/lib/utils';
    import { invalidateAll } from '$app/navigation';
    import { ToastMessage } from '$src/models/toastMessage';

    const storeLeagueType: Writable<LeagueType> = writable(LeagueType.HEAD_TO_HEAD);
    let elm;

    onMount(() => {
        league

        storeLeagueType.subscribe(value => {
            league.config.type = value

            if (value === LeagueType.ROUND_ROBIN) {
                league.config.players = 4;
                league.config.teams = 2;
                league.config.games = 3;
                league.config.scoresPerTeam = 1;
                league.config.playersPerTeam = 2;
                league.config.maxScore = 5
            } else {
                league.config.players = 2;
                league.config.teams = 2;
                league.config.scoresPerTeam = 1;
                league.config.games = 1;
                league.config.playersPerTeam = 1;
                league.config.maxScore = 5
            }
        })
    })

    const newLeague = (): LeagueCreation => {
        const league = new LeagueCreation()

        league.startDate = moment().format('YYYY-MM-DD')

        league.coefficients = new LeagueCoefficients();
        league.coefficients.kValue = 36;
        league.coefficients.resultCoefficient = 50;

        league.config = new LeagueConfig();
        league.config.startingElo = 1000
        league.config.type = LeagueType.HEAD_TO_HEAD
        league.config.players = 2;
        league.config.teams = 2;
        league.config.scoresPerTeam = 1;
        league.config.games = 1;
        league.config.playersPerTeam = 1;

        return league
    }

    const canSubmit = (): boolean => {
        return true;
    }

    const save = async () => {
        const response = await post(`/leagues`, league);

        if (response.ok) {
            await invalidateAll();
            modalStore.close();
            toastStore.trigger(ToastMessage.success('League successfully created'));
        } else {
            toastStore.trigger(ToastMessage.failure('Could not create league'));
        }
    }

    let league = newLeague()

    $: playersPerTeam = league.config.players / league.config.teams
    $: scoreBasedCoefficient = 100 - league.coefficients.resultCoefficient;
</script>

<div class="card card-glass-surface p-6">
  <div class="card-body flex flex-col ">
    <div class="grid grid-cols gap-4">
      <label for="name">
        <span>Name</span>
        <input
            required
            id="name"
            name="name"
            type="text"
            minlength="5"
            bind:this={elm}
            bind:value={league.name}
        />
      </label>

    </div>

    <div class="text-center mt-5 lg:mt-10 mb-5">
      <label>
        <span>League Type</span>
        <RadioGroup selected={storeLeagueType}>
          <RadioItem value={LeagueType.HEAD_TO_HEAD}>Head to Head</RadioItem>
          <RadioItem value={LeagueType.ROUND_ROBIN}>Round Robin</RadioItem>
        </RadioGroup>
      </label>
    </div>

    <div class="grid grid-cols-2 lg:grid-cols-3 gap-4">
      <label for="startingElo">
        <span>Starting Elo</span>
        <input
            required
            id="startingElo"
            name="startingElo"
            type="number"
            min="800"
            max="2000"
            bind:value={league.config.startingElo}
        />
      </label>
      {#if league.config.type === LeagueType.ROUND_ROBIN}
        <label for="scoresPerTeam">
          <span>Games per team</span>
          <input
              required
              id="scoresPerTeam"
              name="scoresPerTeam"
              type="number"
              min="1"
              max="5"
              bind:value={league.config.scoresPerTeam}
          />
        </label>
      {:else }
        <label for="games">
          <span>Games</span>
          <input
              required
              id="games"
              name="games"
              type="number"
              min="1"
              max="9"
              bind:value={league.config.games}
          />
        </label>
      {/if}
      <label for="score">
        <span>Max Score</span>
        <input
            required
            id="score"
            name="score"
            type="number"
            min="1"
            bind:value={league.config.maxScore}
        />
      </label>
      <label for="players">
        <span>Players</span>
        <input
            required
            id="players"
            name="players"
            type="number"
            disabled={league.config.type === LeagueType.ROUND_ROBIN}
            min="2"
            max="10"
            bind:value={league.config.players}
        />
      </label>
      <label for="teams">
        <span>Teams</span>
        <input
            required
            id="teams"
            name="teams"
            type="number"
            disabled
            bind:value={league.config.teams}
        />
      </label>
      <label for="playersPerTeam">
        <span>Players per Team</span>
        <input
            required
            id="playersPerTeam"
            name="playersPerTeam"
            type="number"
            disabled
            value={playersPerTeam}
        />
      </label>
      <label for="kValue">
        <span>K Value</span>
        <input
            required
            id="kValue"
            name="kvalue"
            type="number"
            min="0"
            max="100"
            bind:value={league.coefficients.kValue}
        />
      </label>
      <label for="resultCoefficient">
        <span>Result Coefficient</span>
        <input
            required
            id="resultCoefficient"
            name="resultCoefficient"
            type="number"
            min="0"
            max="100"
            bind:value={league.coefficients.resultCoefficient}
        />
      </label>
      <label for="scoreCoefficient">
        <span>Score Coefficient</span>
        <input
            required
            id="scoreCoefficient"
            name="scoreCoefficient"
            type="number"
            disabled
            value={scoreBasedCoefficient}
        />
      </label>
    </div>
    <button
        class="btn bg-primary-500 btn-lg text-white w-full mt-4"
        on:click={save}
        disabled={!canSubmit}
    >
      Create League
    </button>
  </div>
</div>
