insert into flag
values
  (1, 'Implicit AND ids 1, 2'),
  (2, 'Use BooleanActivFac to AND 1, 2'),
  (3, 'Use BooleanActivFac to OR 1, 2'),
  (4, 'Use AndActivFac to AND 1, 2'),
  (5, 'Use OrActivFac to OR 1, 2'),
  (6, 'featureF');
insert into activation_config
values
  (
    1, '{"startDateTime":"11-12-2023 12:00","endDateTime":"20-12-2023 12:00" }',
    'DateTime', 'org.novi.activations.DateTimeActivationFactory'
  ),
  (
    2, '{"SampleA":100.0,"SampleB":0,"SampleC":0}',
    'Always SAMPLE A', 'org.novi.activations.WeightedRandomActivationFactory'
  ),
  (
    3, '{"activationIds":[1,2],"operation":"AND"}',
    '1 AND 2', 'org.novi.activations.factories.NoviOperationActivationFactory'
  ),
 (
   4, '{"activationIds":[1,2],"operation":"OR"}',
   '1 OR 2', 'org.novi.activations.factories.NoviOperationActivationFactory'
 ),
 (
    5, '[1,2]',
    'DateTimeActivation && WeightedRandomActivation', 'org.novi.core.AndActivationFactory'
  ),
 (
  6, '[1,2]',
  'DateTimeActivation && WeightedRandomActivation', 'org.novi.core.OrActivationFactory'
 );

insert into flag_activation_configs
values
  (1, 1),
  (1, 2),
  (2, 3),
  (3, 4),
  (4, 5),
  (5, 6);